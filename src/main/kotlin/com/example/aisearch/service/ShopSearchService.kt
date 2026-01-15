package com.example.aisearch.service

import com.example.aisearch.model.ShopResultDto
import com.example.aisearch.model.ShopSearchResponse
import com.example.aisearch.repository.MerchantRepository
import com.example.aisearch.repository.ShopSearchRepository
import reactor.core.publisher.Mono

class ShopSearchService(
  private val searchRepository: ShopSearchRepository,
  private val merchantRepository: MerchantRepository,
  private val aiService: AiEmbeddingService
) {

  fun searchNearestShops(
    query: String,
    lat: Double,
    lon: Double,
    radius: Double
  ): Mono<List<ShopSearchResponse>> {
    // 1. Convert natural language query to a vector
    return aiService.getVector(query)
      // 2. Search in ClickHouse
      .flatMapMany { queryVector -> searchRepository.searchNearest(lat, lon, queryVector, radius) }
      .collectList()
      .flatMap { clickhouseResults ->
        // 3. Enrich with Merchant data from H2
        populate(clickhouseResults)
      }
  }

  private fun populate(clickhouseResults: List<ShopResultDto>): Mono<List<ShopSearchResponse>> {
    return if (clickhouseResults.isEmpty()) {
      Mono.just(emptyList())
    } else {
      val merchantIds = clickhouseResults.map { it.merchantId }.distinct()
      merchantRepository.findByIds(merchantIds).map { merchantsMap ->
        clickhouseResults.map {
          ShopSearchResponse(
            id = it.id,
            distance = it.distance,
            score = it.score,
            address = it.address,
            merchant = merchantsMap[it.merchantId]
          )
        }
      }
    }
  }
}
