package com.example.aisearch.index

import com.example.aisearch.service.AiEmbeddingService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DataInitializer(
  private val jdbcTemplate: NamedParameterJdbcTemplate,
  private val clickHouseTemplate: NamedParameterJdbcTemplate,
  private val embeddingService: AiEmbeddingService
) : CommandLineRunner {

  private val logger = LoggerFactory.getLogger(javaClass)

  override fun run(vararg args: String?) {
    logger.info("Starting full synchronization...")
    recreateIndex()
      .thenMany(findSyncData())
      .flatMap { record ->
        val combinedText = getCombinedDescription(record)
        embeddingService.getVector(combinedText)
          .map { vector ->
            val sql =
              "INSERT INTO shop_search_index (shop_id, merchant_id, merchant_name, address, location, embedding) VALUES (:shop_id, :merchant_id, :merchant_name, :address, tuple(:lon, :lat), :embedding)"
            val params = mapOf(
              "shop_id" to record["shop_id"],
              "merchant_id" to record["merchant_id"],
              "merchant_name" to record["name"],
              "address" to (record["address"] ?: ""),
              "lon" to record["longitude"],
              "lat" to record["latitude"],
              "embedding" to vector.toTypedArray()
            )
            clickHouseTemplate.update(sql, params)
          }
          .then()
      }
      .then()
      .doOnSuccess { logger.info("Full synchronization completed successfully.") }
      .doOnError { e -> logger.error("Synchronization failed: ${e.message}", e) }
      .subscribe()
  }

  private fun getCombinedDescription(record: Map<String, Any?>): String {
    val name = record["name"] as String
    val description = record["description_eng"] as? String ?: ""
    val category = record["category_name"] as? String ?: ""
    val parentCatName = record["parent_category_name"] as? String
    val address = record["address"] as? String ?: ""
    val city = record["city"] as? String ?: ""
    val district = record["district"] as? String ?: ""

    val fullCategoryPath = if (parentCatName != null) "$parentCatName > $category" else category
    val rawText = "$name $fullCategoryPath $description $address $city $district"
    return rawText.replace(Regex("\\s+"), " ").trim()
  }

  fun recreateIndex(): Mono<Void> = Mono.fromRunnable {
    clickHouseTemplate.jdbcTemplate.execute("DROP TABLE IF EXISTS shop_search_index")
    clickHouseTemplate.jdbcTemplate.execute(
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


  fun findSyncData(): Flux<Map<String, Any?>> {
    val sql = """
      SELECT 
        m.id as merchant_id,
        m.name,
        m.description_eng,
        c.name_eng as category_name,
        p.name_eng as parent_category_name,
        s.id as shop_id,
        s.latitude,
        s.longitude,
        s.city,
        s.district,
        CONCAT(s.street, ' ', s.building) as address
      FROM merchant m
      JOIN merchant_category c ON m.category_id = c.id
      LEFT JOIN merchant_category p ON c.parent_id = p.id
      JOIN shop s ON s.merchant_id = m.id
    """.trimIndent()

    return Flux.fromIterable(jdbcTemplate.queryForList(sql, emptyMap<String, Any>()))
  }
}
