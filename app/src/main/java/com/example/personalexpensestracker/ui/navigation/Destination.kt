package com.example.personalexpensestracker.ui.navigation




import com.example.personalexpensestracker.R

sealed class Destination(val route: String, val icon: Int) {
    object Dashboard: Destination("dashboard", R.drawable.outline_home_24)
    object AddTransaction: Destination("add_transaction",R.drawable.outline_add_24)
    object History: Destination("history",R.drawable.outline_history_2_24)
    object Settings: Destination("settings",R.drawable.outline_settings_24)
}

val navItemList = listOf(
    Destination.Dashboard,
    Destination.AddTransaction,
    Destination.History,
    Destination.Settings
)