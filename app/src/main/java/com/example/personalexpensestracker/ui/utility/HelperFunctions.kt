package com.example.personalexpensestracker.ui.utility




import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.personalexpensestracker.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.Locale


private val dateFormatter = SimpleDateFormat(
    "dd/MM/yyyy HH:mm",
    Locale.getDefault()
)

fun Long.toFormattedDate(): String {
    return dateFormatter.format(Date(this))
}

fun Long.toTimeAgo(): String {
    val now = Instant.now()
    val time = Instant.ofEpochMilli(this)

    val duration = Duration.between(time, now)

    val seconds = duration.seconds
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        else -> this.toFormattedDate() // fallback to full date
    }
}

@Composable
fun TransactionCategory.icon(): Painter {
    return when (this) {
        TransactionCategory.FOOD         -> painterResource(R.drawable.outline_local_pizza_24)
        TransactionCategory.TRANSPORT    -> painterResource(R.drawable.outline_directions_bus_24)
        TransactionCategory.SHOPPING     -> painterResource(R.drawable.outline_shopping_cart_24)
        TransactionCategory.HEALTH       -> painterResource(R.drawable.outline_health_and_safety_24)
        TransactionCategory.ENTERTAINMENT -> painterResource(R.drawable.outline_sports_football_24)
        TransactionCategory.SALARY       -> painterResource(R.drawable.outline_money_24)
        TransactionCategory.FREELANCE    -> painterResource(R.drawable.outline_attach_money_24)
        TransactionCategory.BUSINESS     -> painterResource(R.drawable.baseline_business_center_24)
        TransactionCategory.GIFT         -> painterResource(R.drawable.outline_featured_seasonal_and_gifts_24)
        TransactionCategory.EXPENSE_OTHER -> painterResource(R.drawable.outline_other_admission_24)
        TransactionCategory.INCOME_OTHER  -> painterResource(R.drawable.outline_other_admission_24)
    }
}