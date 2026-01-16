package com.example.aisearch.repository

import com.example.aisearch.model.MerchantInfo
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class MerchantRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

  fun findByIds(ids: List<Long>): Mono<Map<Long, MerchantInfo>> {
    if (ids.isEmpty()) return Mono.just(emptyMap())

    return Mono.fromCallable {
      val sql = "SELECT id, name, description_eng FROM merchant WHERE id IN (:ids)"
      val params = mapOf("ids" to ids)

      jdbcTemplate.query(sql, params) { rs, _ ->
        MerchantInfo(
          id = rs.getLong("id"),
          name = rs.getString("name"),
          descriptionEng = rs.getString("description_eng")
        )
      }.associateBy { it.id }
    }.subscribeOn(Schedulers.boundedElastic())
  }
}
