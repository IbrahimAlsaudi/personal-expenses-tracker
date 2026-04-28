package com.example.personalexpensestracker.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensestracker.data.repository.UserPreferences
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings screen.
 * Best practice: Use DataStore as the single source of truth and expose state via StateFlow.
 */
@OptIn(FlowPreview::class)
class SettingsViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val budgetQuery = MutableStateFlow("")




    val uiState: StateFlow<SettingsUiState> = combine(
        budgetQuery,
        userPreferences.allowDailySummary,
        userPreferences.budgetAlert,
        userPreferences.showExchangeRate
    ) { budget, dailySummary, alert, showExchange ->
        SettingsUiState(
            monthlyBudget = budget,
            dailySummary = dailySummary,
            budgetAlert = alert,
            showExchangeRate = showExchange
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun onMonthlyBudgetChanged(budget: String) {
        budgetQuery.value = budget
    }

    fun onDailySummaryChanged(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.saveAllowDailySummary(enabled)
        }
    }

    fun onBudgetAlertChanged(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.saveAllowBudgetAlert(enabled)
        }
    }

    fun onShowExchangeRateChanged(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.saveShowExchangeRate(enabled)
        }
    }

    init {
        viewModelScope.launch {
            val savedBudget = userPreferences.monthlyBudget.first()
            if(budgetQuery.value.isEmpty()) {
                budgetQuery.value = if(savedBudget == 0.0) "" else savedBudget.toString()
            }
        }

        viewModelScope.launch {
            budgetQuery
                .debounce(500L)
                .distinctUntilChanged()
                .collect {
                    userPreferences.saveMonthlyBudget(it.toDoubleOrNull() ?: 0.0)
                }
        }
    }
}

/**
 * UI State for the Settings screen.
 */
data class SettingsUiState(
    val monthlyBudget: String = "",
    val dailySummary: Boolean = true,
    val budgetAlert: Boolean = true,
    val showExchangeRate: Boolean = true,
)
