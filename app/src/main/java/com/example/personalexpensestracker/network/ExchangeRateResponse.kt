package com.example.personalexpensestracker.network

data class ExchangeRateResponse(
    val base: String,
    val rates: Map<String,Double>
)
