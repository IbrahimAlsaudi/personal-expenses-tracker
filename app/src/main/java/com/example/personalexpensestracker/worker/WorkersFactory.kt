package com.example.personalexpensestracker.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.UserPreferences

class FinanceWorkersFactory(
    private val transactionRepository: TransactionRepository,
    private val userPreferences: UserPreferences,
    private val exchangeRateRepository: ExchangeRateRepository,
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            BudgetLimitReminder::class.java.name ->
                BudgetLimitReminder(
                    appContext,
                    workerParameters,
                    userPreferences,
                    transactionRepository
                )

            ExchangeRateWorker::class.java.name ->
                ExchangeRateWorker(
                    appContext,
                    workerParameters,
                    exchangeRateRepository,
                    userPreferences
                )

            DailySummaryReminder::class.java.name ->
                DailySummaryReminder(
                    appContext,
                    workerParameters,
                    userPreferences,
                    transactionRepository
                )
            else -> null // Return null to let the default factory handle it
        }
    }
}