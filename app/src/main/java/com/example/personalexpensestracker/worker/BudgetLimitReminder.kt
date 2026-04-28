package com.example.personalexpensestracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import kotlinx.coroutines.flow.first

class BudgetLimitReminder(
    context: Context,
    workerParameters: WorkerParameters,
    private val userPreferences: UserPreferences,
    private val transactionRepository: TransactionRepository,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val isAlertEnabled = userPreferences.budgetAlert.first()
        if (!isAlertEnabled) return Result.success()

        val monthlyBudget = userPreferences.monthlyBudget.first()
        if (monthlyBudget <= 0.0) return Result.success()

        val (start, end) = getMonthBounds()
        val currentExpenses = transactionRepository.getMonthlyExpenses(start, end)


        if (currentExpenses >= (monthlyBudget * 0.9)) {
            val remaining = monthlyBudget - currentExpenses
            val message = if (remaining > 0) {
                "You have spent 90% of your budget. $remaining left for this month."
            } else {
                "You have exceeded your monthly budget by ${-remaining}!"
            }

            makeNotification(
                title = "Budget Alert",
                message = message,
                type = NotificationType.BudgetAlert,
                context = applicationContext
            )
        }

        return Result.success()
    }
}
