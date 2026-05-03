package com.example.personalexpensestracker.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ExchangeRateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userPreferences: UserPreferences
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val isRatesEnabled = userPreferences.showExchangeRate.first()
        if (!isRatesEnabled) return Result.success()

        exchangeRateRepository.refreshRates()

        return Result.success()
    }

}