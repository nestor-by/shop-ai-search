package com.example.aisearch.service

import com.example.aisearch.model.OllamaEmbeddingRequest
import com.example.aisearch.model.OllamaEmbeddingResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class OllamaAiEmbeddingService(
  private val ollamaUrl: String,
  private val ollamaModel: String
) : AiEmbeddingService {
  private val logger = LoggerFactory.getLogger(javaClass)
  private val webClient = WebClient.builder()
    .baseUrl(ollamaUrl)
    .build()

  override fun getVector(query: String): Mono<List<Float>> {
    val startTime = System.currentTimeMillis()
    val cleanedQuery = cleanText(query)
    val request = OllamaEmbeddingRequest(model = ollamaModel, prompt = cleanedQuery)
    val body = jacksonObjectMapper().writeValueAsString(request)
    return webClient.post()
      .uri("/api/embeddings")
      .bodyValue(body)
      .retrieve()
      .bodyToMono(OllamaEmbeddingResponse::class.java)
      .map { it.embedding }
      .doOnSuccess {
        val duration = System.currentTimeMillis() - startTime
        logger.info("getVector for query [{}], duration: {}ms", body, duration)
      }
      .doOnError { e ->
        val duration = System.currentTimeMillis() - startTime
        logger.error("getVector failed for query [{}], duration: {}ms, error: {}", body, duration, e.message)
      }
  }

  private fun cleanText(text: String): String {
    return text.replace(Regex("[^\\p{L}\\p{N}\\s,.-]"), " ")
      .replace(Regex("\\s+"), " ")
      .trim()
  }
}
