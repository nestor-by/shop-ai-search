package com.example.aisearch.service

import reactor.core.publisher.Mono

interface AiEmbeddingService {
  fun getVector(query: String): Mono<List<Float>>
}
