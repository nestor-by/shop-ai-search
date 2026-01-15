package com.example.aisearch.repository

import com.example.aisearch.jooq.tables.references.MERCHANT
import com.example.aisearch.model.MerchantInfo
import org.jooq.DSLContext
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MerchantRepository(private val dslContext: DSLContext) {

  fun findByIds(ids: List<Long>): Mono<Map<Long, MerchantInfo>> {
    val query = dslContext.selectFrom(MERCHANT)
      .where(MERCHANT.ID.`in`(ids))

    return Flux.from(query)
      .map {
        MerchantInfo(
          id = it.id!!,
          name = it.name!!,
          descriptionEng = it.descriptionEng
        )
      }
      .collectList()
      .map { list -> list.associateBy { it.id } }
  }
}
