package com.example.personalexpensestracker.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.network.ExchangeRateResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class DashboardViewModel(
    private val transactionRepository: TransactionRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userPreferences: UserPreferences,
): ViewModel() {


    private val showExchangeRates = userPreferences.showExchangeRate
    private val _totalIncome = transactionRepository.getTotalIncome().distinctUntilChanged()
    private val _totalExpenses = transactionRepository.getTotalExpense().distinctUntilChanged()


    private val _recentTransactions = transactionRepository.getRecentTransactions().distinctUntilChanged()

    private val _exchangeRates = exchangeRateRepository.getExchangeRates().distinctUntilChanged()

//    init{
//        viewModelScope.launch {
//            if(showExchangeRates.first()){
//                exchangeRateRepository.refreshRates()
//            }
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = showExchangeRates.flatMapLatest { isEnabled ->
        combine(_totalIncome,
            _totalExpenses,
            _recentTransactions,
            if(isEnabled) _exchangeRates else flowOf(emptyList()),
        )
        { totalIncome, totalExpenses, recentTransactions, exchangeRates ->
            DashboardUiState(
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                totalBalance = totalIncome - totalExpenses,
                recentTransactions = recentTransactions,
                exchangeRates = exchangeRates,
                showExchangeRates = isEnabled
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}

data class DashboardUiState(
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val exchangeRates: List<ExchangeRate> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val showExchangeRates: Boolean = true
    )