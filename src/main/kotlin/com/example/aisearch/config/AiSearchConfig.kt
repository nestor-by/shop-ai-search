package com.example.aisearch.config

import com.example.aisearch.controller.ShopController
import com.example.aisearch.index.DataInitializer
import com.example.aisearch.repository.MerchantRepository
import com.example.aisearch.repository.ShopSearchRepository
import com.example.aisearch.service.AiEmbeddingService
import com.example.aisearch.service.HuggingfaceAiEmbeddingService
import com.example.aisearch.service.ShopSearchService
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
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
  @Qualifier("dslContext")
  fun dslContext(@Qualifier("dataSource") dataSource: DataSource): DSLContext {
    return DSL.using(dataSource, SQLDialect.H2)
  }

  @Bean
  @Qualifier("clickhouseDslContext")
  fun clickhouseDslContext(@Qualifier("clickhouseDataSource") dataSource: DataSource): DSLContext {
    return DSL.using(dataSource, SQLDialect.CLICKHOUSE)
  }

  @Bean
  fun merchantRepository(@Qualifier("dslContext") dslContext: DSLContext): MerchantRepository {
    return MerchantRepository(dslContext)
  }

  @Bean
  fun shopSearchRepository(@Qualifier("clickhouseDslContext") dslContext: DSLContext): ShopSearchRepository {
    return ShopSearchRepository(dslContext)
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
    @Qualifier("dslContext") dslContext: DSLContext,
    @Qualifier("clickhouseDslContext") clickhouseDslContext: DSLContext,
    embeddingService: AiEmbeddingService
  ): DataInitializer {
    return DataInitializer(dslContext, clickhouseDslContext, embeddingService)
  }
}
