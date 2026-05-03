package com.example.personalexpensestracker.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class DailySummaryReminder @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val userPreferences: UserPreferences,
    private val transactionRepository: TransactionRepository
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        
        
        val isDailySummaryEnabled = userPreferences.allowDailySummary.first()
        if (!isDailySummaryEnabled) return Result.success()
        
        val (start, end) = getDayBounds()
        val todayExpenses = transactionRepository.getMonthlyExpenses(start, end)
        makeNotification(
            title = "Daily Summary",
            message = "You have spent $todayExpenses EGP Today",
            type = NotificationType.DailySummary,
            context = applicationContext
        )
        return Result.success()
    }

}