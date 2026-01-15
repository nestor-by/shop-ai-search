package com.example.aisearch.model

data class ShopResultDto(
    val id: Long,
    val merchantId: Long,
    val distance: Double,
    val score: Double,
    val address: String?
)
