package com.example.aisearch.service

import com.example.aisearch.model.HuggingfaceEmbeddingRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class HuggingfaceAiEmbeddingService(
  private val url: String,
  private val model: String
) : AiEmbeddingService {
  private val logger = LoggerFactory.getLogger(javaClass)
  private val webClient = WebClient.builder()
    .baseUrl(url)
    .build()

  override fun getVector(query: String): Mono<List<Float>> {
    val startTime = System.currentTimeMillis()
    val cleanedQuery = cleanText(query)
    val request = HuggingfaceEmbeddingRequest(model = model, inputs = listOf(cleanedQuery))
    val body = jacksonObjectMapper().writeValueAsString(request)
    return webClient.post()
      .uri("/embed")
      .header("Content-Type", "application/json")
      .bodyValue(body)
      .retrieve()
      .bodyToMono(Array<FloatArray>::class.java)
      .map { it[0].toList() }
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
