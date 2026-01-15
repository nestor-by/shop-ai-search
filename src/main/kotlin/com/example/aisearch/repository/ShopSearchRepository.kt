package com.example.aisearch.repository

import com.example.aisearch.model.ShopResultDto
import org.jooq.DSLContext
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

class ShopSearchRepository(
  private val dslContext: DSLContext
) {

  fun searchNearest(
    userLat: Double,
    userLon: Double,
    queryVector: List<Float>,
    radius: Double
  ): Flux<ShopResultDto> {
    return Flux.defer {
      val results = dslContext.fetch(sql, userLon, userLat, queryVector.toTypedArray(), radius)
      Flux.fromIterable(results.map { rs ->
        ShopResultDto(
          id = rs.get("shop_id", Long::class.java)!!,
          merchantId = rs.get("merchant_id", Long::class.java)!!,
          distance = rs.get("distance", Double::class.java)!!,
          score = 1.0 - rs.get("ai_score", Double::class.java)!!,
          address = rs.get("address", String::class.java)
        )
      })
    }.subscribeOn(Schedulers.boundedElastic())
  }

  companion object {
    private val sql = """
            SELECT 
                shop_id,
                merchant_id,
                address,
                greatCircleDistance(?, ?, location.1, location.2) as distance,
                cosineDistance(embedding, ?) as ai_score
            FROM shop_search_index
            WHERE distance <= ? 
            ORDER BY ai_score ASC, distance ASC
            LIMIT 20
        """.trimIndent()
  }
}
