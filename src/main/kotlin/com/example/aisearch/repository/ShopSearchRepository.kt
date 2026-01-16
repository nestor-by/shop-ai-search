package com.example.aisearch.repository

import com.example.aisearch.model.ShopResultDto
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

class ShopSearchRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {

  fun searchNearest(
    userLat: Double,
    userLon: Double,
    queryVector: List<Float>,
    radius: Double
  ): Flux<ShopResultDto> {
    return Flux.defer {
      val params = mapOf(
        "lon" to userLon,
        "lat" to userLat,
        "embedding" to queryVector.toTypedArray(),
        "radius" to radius
      )
      val results = jdbcTemplate.query(sql, params) { rs, _ ->
        ShopResultDto(
          id = rs.getLong("shop_id"),
          merchantId = rs.getLong("merchant_id"),
          distance = rs.getDouble("distance"),
          score = 1.0 - rs.getDouble("ai_score"),
          address = rs.getString("address")
        )
      }
      Flux.fromIterable(results)
    }.subscribeOn(Schedulers.boundedElastic())
  }

  companion object {
    private val sql = """
            SELECT 
                shop_id,
                merchant_id,
                address,
                greatCircleDistance(:lon, :lat, location.1, location.2) as distance,
                cosineDistance(embedding, :embedding) as ai_score
            FROM shop_search_index
            WHERE distance <= :radius 
            ORDER BY ai_score ASC, distance ASC
            LIMIT 20
        """.trimIndent()
  }
}
