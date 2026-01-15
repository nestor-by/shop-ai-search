package com.example.aisearch.model

data class HuggingfaceEmbeddingRequest(
    val model: String,
    val inputs: List<String>
)
