package com.example.personalexpensestracker

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.personalexpensestracker.ui.screens.addtransaction.AddTransactionViewModel
import com.example.personalexpensestracker.ui.screens.dashboard.DashboardViewModel
import com.example.personalexpensestracker.ui.screens.history.HistoryViewModel
import com.example.personalexpensestracker.ui.screens.settings.SettingsViewModel


object AppViewModelProvider {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            DashboardViewModel(
                transactionRepository = financeApplication().container.transactionRepository,
                exchangeRateRepository = financeApplication().container.exchangeRateRepository,
                userPreferences = financeApplication().container.userPreferences
            )
        }

        initializer {
            AddTransactionViewModel(
                transactionRepository = financeApplication().container.transactionRepository,
                workManagerRepository = financeApplication().container.workManagerRepository
            )
        }

        initializer {
            HistoryViewModel(
                transactionRepository = financeApplication().container.transactionRepository
            )
        }

        initializer {
            SettingsViewModel(
                userPreferences = financeApplication().container.userPreferences
            )
        }
    }
}

fun CreationExtras.financeApplication(): FinanceApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FinanceApplication)