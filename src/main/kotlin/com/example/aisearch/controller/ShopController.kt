package com.example.aisearch.controller

import com.example.aisearch.model.ShopSearchResponse
import com.example.aisearch.service.ShopSearchService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/shops")
class ShopController(private val shopSearchService: ShopSearchService) {

    @GetMapping("/search")
    fun search(
        @RequestParam query: String,
        @RequestParam lat: Double,
        @RequestParam lon: Double,
        @RequestParam(defaultValue = "10.0") radius: Double
    ): Mono<List<ShopSearchResponse>> {
        return shopSearchService.searchNearestShops(query, lat, lon, radius)
    }
}
