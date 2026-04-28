package com.example.personalexpensestracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import kotlinx.coroutines.flow.first

class ExchangeRateWorker(

    private val exchangeRateRepository: ExchangeRateRepository,
    context: Context,
    workerParameters: WorkerParameters,
    private val userPreferences: UserPreferences
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val isRatesEnabled = userPreferences.showExchangeRate.first()
        if (!isRatesEnabled) return Result.success()

        exchangeRateRepository.refreshRates()
        return Result.success()
    }

}