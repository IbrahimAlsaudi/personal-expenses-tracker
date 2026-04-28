package com.example.personalexpensestracker.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.personalexpensestracker.worker.BudgetLimitReminder
import com.example.personalexpensestracker.worker.DailySummaryReminder
import com.example.personalexpensestracker.worker.ExchangeRateWorker
import java.util.concurrent.TimeUnit

interface WorkManagerRepository {
    fun scheduleExchangeRateRefresh()
    fun scheduleDailySummary()
    fun scheduleBudgetAlert()
}

class WorkManagerRepositoryImpl(context: Context): WorkManagerRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleExchangeRateRefresh() {
        val request = PeriodicWorkRequestBuilder<ExchangeRateWorker>(
            2, TimeUnit.HOURS
        ).setConstraints(Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        ).build()
        
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "Exchange Rate Work",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = request
        )
    }

    override fun scheduleDailySummary() {
        val dailyRequest = PeriodicWorkRequestBuilder<DailySummaryReminder>(
            24, TimeUnit.HOURS
        ).build()
        
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "Daily Summary Work",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = dailyRequest
        )
    }

    override fun scheduleBudgetAlert() {
        // One-time check used reactively when a transaction is added
        val request = OneTimeWorkRequestBuilder<BudgetLimitReminder>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        
        // Use REPLACE so that rapid transactions only trigger one check
        workManager.enqueueUniqueWork(
            uniqueWorkName = "Budget Alert Work",
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request
        )
    }
}
