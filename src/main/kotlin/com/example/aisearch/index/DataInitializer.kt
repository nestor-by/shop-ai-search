package com.example.aisearch.index

import com.example.aisearch.jooq.tables.references.MERCHANT
import com.example.aisearch.jooq.tables.references.MERCHANT_CATEGORY
import com.example.aisearch.jooq.tables.references.SHOP
import com.example.aisearch.service.AiEmbeddingService
import org.jooq.DSLContext
import org.jooq.Record
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DataInitializer(
  private val dslContext: DSLContext,
  private val clickHouseContext: DSLContext,
  private val embeddingService: AiEmbeddingService
) : CommandLineRunner {

  private val logger = LoggerFactory.getLogger(javaClass)

  override fun run(vararg args: String?) {
    logger.info("Starting full synchronization...")
    recreateIndex()
      .thenMany<Record>(findSyncData())
      .flatMap<Void> { record ->
        val combinedText = getCombinedDescription(record)
        embeddingService.getVector(combinedText)
          .map<Int> { vector ->
            val sql =
              "INSERT INTO shop_search_index (shop_id, merchant_id, merchant_name, address, location, embedding) VALUES (?, ?, ?, ?, tuple(?, ?), ?)"
            clickHouseContext.execute(
              sql,
              record.get<Long>("shop_id", Long::class.java)!!,
              record.get<Long>("merchant_id", Long::class.java)!!,
              record.get<String>("NAME", String::class.java)!!,
              record.get<String>("address", String::class.java) ?: "",
              record.get<Double>("LONGITUDE", Double::class.java)!!,
              record.get<Double>("LATITUDE", Double::class.java)!!,
              vector.toTypedArray<Float>()
            )
          }
          .then()
      }
      .then()
      .doOnSuccess { logger.info("Full synchronization completed successfully.") }
      .doOnError { e -> logger.error("Synchronization failed: ${e.message}", e) }
      .subscribe()
  }

  private fun getCombinedDescription(record: Record): String {
    val description = record.get("DESCRIPTION_ENG", String::class.java) ?: ""
    val category = record.get("category_name", String::class.java) ?: ""
    val parentCatName = record.get("parent_category_name", String::class.java)
    val address = record.get("address", String::class.java) ?: ""
    val city = record.get(SHOP.CITY) ?: ""
    val district = record.get(SHOP.DISTRICT) ?: ""

    val fullCategoryPath = if (parentCatName != null) "$parentCatName > $category" else category
    val rawText = "${record.get("NAME", String::class.java)!!} $fullCategoryPath $description $address $city $district"
    return rawText.replace(Regex("\\s+"), " ").trim()
  }

  fun recreateIndex(): Mono<Void> = Mono.fromRunnable {
    clickHouseContext.execute("DROP TABLE IF EXISTS shop_search_index")
    clickHouseContext.execute(
      """
              CREATE TABLE shop_search_index (
                  shop_id Int64,
                  merchant_id Int64,
                  merchant_name String,
                  address String,
                  location Point,
                  embedding Array(Float32)
              ) ENGINE = MergeTree()
              ORDER BY (merchant_id, shop_id)
          """.trimIndent()
    )
  }


  fun findSyncData(): Flux<Record> {
    val m = MERCHANT.`as`("m")
    val c = MERCHANT_CATEGORY.`as`("c")
    val p = MERCHANT_CATEGORY.`as`("p")
    val s = SHOP.`as`("s")

    val query = dslContext.select(
      m.ID.`as`("merchant_id"),
      m.NAME,
      m.DESCRIPTION_ENG,
      c.NAME_ENG.`as`("category_name"),
      p.NAME_ENG.`as`("parent_category_name"),
      s.ID.`as`("shop_id"),
      s.LATITUDE,
      s.LONGITUDE,
      s.CITY,
      s.DISTRICT,
      s.STREET.concat(" ").concat(s.BUILDING).`as`("address")
    ).from(m)
      .join(c).on(m.CATEGORY_ID.eq(c.ID))
      .leftJoin(p).on(c.PARENT_ID.eq(p.ID))
      .join(s).on(s.MERCHANT_ID.eq(m.ID))

    return Flux.from(query)
  }
}
