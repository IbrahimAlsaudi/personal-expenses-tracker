package com.example.personalexpensestracker.network

import retrofit2.http.GET

interface FinanceApiService {

    @GET("v6/latest/EGP")
    suspend fun getExchangeRates(): ExchangeRateResponse
}