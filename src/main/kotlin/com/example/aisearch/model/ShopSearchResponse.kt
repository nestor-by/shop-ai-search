package com.example.aisearch.model

data class ShopSearchResponse(
    val id: Long,
    val distance: Double,
    val score: Double,
    val address: String? = null,
    val merchant: MerchantInfo? = null
)
