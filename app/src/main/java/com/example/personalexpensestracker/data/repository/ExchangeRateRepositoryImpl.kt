package com.example.personalexpensestracker.data.repository

import com.example.personalexpensestracker.data.room.dao.ExchangeRateDao
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import com.example.personalexpensestracker.network.FinanceApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
    private val apiService: FinanceApiService
) : ExchangeRateRepository {

    override fun getExchangeRates(): Flow<List<ExchangeRate>> = exchangeRateDao.getExchangeRates()
    override suspend fun refreshRates() {

        val response = apiService.getExchangeRates()
        val rates = listOf(
            ExchangeRate("USD", response.rates["USD"] ?: 0.0, System.currentTimeMillis()),
            ExchangeRate("EUR", response.rates["EUR"] ?: 0.0, System.currentTimeMillis()),
            ExchangeRate("GBP", response.rates["GBP"] ?: 0.0, System.currentTimeMillis())
        )

        exchangeRateDao.insertExchangeRate(rates)
    }
}