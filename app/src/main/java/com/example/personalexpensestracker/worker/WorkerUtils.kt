package com.example.personalexpensestracker.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.util.Calendar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.personalexpensestracker.MainActivity
import com.example.personalexpensestracker.R

/**
 * Defines the configuration for different types of notifications in the app.
 * This sealed class makes it easy to add new notification types without changing
 * the core notification logic.
 */
sealed class NotificationType(
    val channelId: String,
    val channelName: String,
    val importance: Int,
    val priority: Int,
    val notificationId: Int,
    val description: String
) {
    object BudgetAlert : NotificationType(
        channelId = "budget_alerts",
        channelName = "Budget Alerts",
        importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else 4,
        priority = NotificationCompat.PRIORITY_HIGH,
        notificationId = 1,
        description = "Notifications when you are close to your spending limit"
    )

    object DailySummary : NotificationType(
        channelId = "daily_summary",
        channelName = "Daily Summary",
        importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else 4,
        priority = NotificationCompat.PRIORITY_HIGH,
        notificationId = 2,
        description = "A daily recap of your expenses"
    )
}

/**
 * Creates a PendingIntent to open the MainActivity when the notification is clicked.
 */
fun createPendingIntent(context: Context): PendingIntent {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    return PendingIntent.getActivity(context, 0, intent, flags)
}

/**
 * Builds and shows a notification based on the provided [NotificationType].
 */
fun makeNotification(
    title: String,
    message: String,
    type: NotificationType,
    context: Context
) {
    // 1. Create the NotificationChannel for Android O+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(type.channelId, type.channelName, type.importance).apply {
            description = type.description
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // 2. Build the notification
    val builder = NotificationCompat.Builder(context, type.channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(type.priority)
        .setContentIntent(createPendingIntent(context))
        .setAutoCancel(true)

    // 3. Show the notification (checking permission for Android 13+)
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

/**
 * Calculates the start and end timestamps (in milliseconds) for the current month.
 * Uses java.util.Calendar for API 24 compatibility.
 *
 * @return A Pair where the first value is the start of the month (00:00:00.000)
 *         and the second value is the end of the month (23:59:59.999).
 */
fun getMonthBounds(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    // Start of month
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startOfMonth = calendar.timeInMillis

    // End of month
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endOfMonth = calendar.timeInMillis

    return Pair(startOfMonth, endOfMonth)
}

fun getDayBounds(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    // Start of day (00:00:00)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startOfDay = calendar.timeInMillis

    // End of day (23:59:59)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endOfDay = calendar.timeInMillis

    return Pair(startOfDay, endOfDay)
}