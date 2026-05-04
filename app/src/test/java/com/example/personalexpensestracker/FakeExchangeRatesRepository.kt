package com.example.personalexpensestracker

import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeExchangeRateRepository : ExchangeRateRepository {

    private val _exchangeRates = MutableStateFlow<List<ExchangeRate>>(emptyList())

    fun emit(list: List<ExchangeRate>) { _exchangeRates.value = list }

    override fun getExchangeRates(): Flow<List<ExchangeRate>> = _exchangeRates

    override suspend fun refreshRates() {}
}