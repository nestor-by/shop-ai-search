package com.example.aisearch.config

import com.example.aisearch.controller.ShopController
import com.example.aisearch.index.DataInitializer
import com.example.aisearch.repository.MerchantRepository
import com.example.aisearch.repository.ShopSearchRepository
import com.example.aisearch.service.AiEmbeddingService
import com.example.aisearch.service.HuggingfaceAiEmbeddingService
import com.example.aisearch.service.ShopSearchService
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
class AiSearchConfig {

  @Bean
  @Primary
  @ConfigurationProperties("spring.mysql.datasource")
  fun dataSource(): HikariDataSource {
    return HikariDataSource()
  }

  @Bean
  @ConfigurationProperties("spring.clickhouse.datasource")
  fun clickhouseDataSource(): HikariDataSource {
    return HikariDataSource()
  }

  @Bean
  @Primary
  @Qualifier("mysqlJdbcTemplate")
  fun mysqlJdbcTemplate(@Qualifier("dataSource") dataSource: DataSource): NamedParameterJdbcTemplate {
    return NamedParameterJdbcTemplate(dataSource)
  }

  @Bean
  @Qualifier("clickhouseJdbcTemplate")
  fun clickhouseJdbcTemplate(@Qualifier("clickhouseDataSource") dataSource: DataSource): NamedParameterJdbcTemplate {
    return NamedParameterJdbcTemplate(dataSource)
  }

  @Bean
  fun merchantRepository(@Qualifier("mysqlJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate): MerchantRepository {
    return MerchantRepository(jdbcTemplate)
  }

  @Bean
  fun shopSearchRepository(@Qualifier("clickhouseJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate): ShopSearchRepository {
    return ShopSearchRepository(jdbcTemplate)
  }

  @Bean
  fun aiEmbeddingService(
    @Value("\${huggingface.url}") url: String,
    @Value("\${huggingface.model}") model: String
  ): AiEmbeddingService {
    return HuggingfaceAiEmbeddingService(url, model)
  }


  @Bean
  fun shopController(
    searchRepository: ShopSearchRepository,
    merchantRepository: MerchantRepository,
    aiService: AiEmbeddingService
  ): ShopController {
    val shopSearchService = ShopSearchService(searchRepository, merchantRepository, aiService)
    return ShopController(shopSearchService)
  }

  @Bean
  fun dataInitializer(
    @Qualifier("mysqlJdbcTemplate") jdbcTemplate: NamedParameterJdbcTemplate,
    @Qualifier("clickhouseJdbcTemplate") clickhouseJdbcTemplate: NamedParameterJdbcTemplate,
    embeddingService: AiEmbeddingService
  ): DataInitializer {
    return DataInitializer(jdbcTemplate, clickhouseJdbcTemplate, embeddingService)
  }
}
