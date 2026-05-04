package com.example.personalexpensestracker

import com.example.personalexpensestracker.data.repository.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserPreferences : UserPreferences {

    private val _showExchangeRate = MutableStateFlow(true)
    private val _monthlyBudget = MutableStateFlow(0.0)
    private val _allowDailySummary = MutableStateFlow(false)
    private val _budgetAlert = MutableStateFlow(false)

    fun setShowExchangeRate(value: Boolean) { _showExchangeRate.value = value }
    fun setMonthlyBudget(value: Double) { _monthlyBudget.value = value }

    override val showExchangeRate: Flow<Boolean> = _showExchangeRate
    override val monthlyBudget: Flow<Double> = _monthlyBudget
    override val allowDailySummary: Flow<Boolean> = _allowDailySummary
    override val budgetAlert: Flow<Boolean> = _budgetAlert

    override suspend fun saveMonthlyBudget(budget: Double) { _monthlyBudget.value = budget }
    override suspend fun saveAllowDailySummary(dailySummary: Boolean) { _allowDailySummary.value = dailySummary }
    override suspend fun saveAllowBudgetAlert(budgetAlert: Boolean) { _budgetAlert.value = budgetAlert }
    override suspend fun saveShowExchangeRate(showExchangeRate: Boolean) { _showExchangeRate.value = showExchangeRate }
}