package com.example.personalexpensestracker.data.repository

import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {

    fun getExchangeRates(): Flow<List<ExchangeRate>>
    suspend fun refreshRates()
}