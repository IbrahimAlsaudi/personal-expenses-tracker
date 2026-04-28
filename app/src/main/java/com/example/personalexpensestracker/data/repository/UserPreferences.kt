package com.example.personalexpensestracker.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.work.impl.model.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface UserPreferences {
    val monthlyBudget: Flow<Double>
    val allowDailySummary: Flow<Boolean>
    val budgetAlert: Flow<Boolean>
    val showExchangeRate: Flow<Boolean>
    suspend fun saveMonthlyBudget(budget: Double)
    suspend fun saveAllowDailySummary(dailySummary: Boolean)
    suspend fun saveAllowBudgetAlert(budgetAlert: Boolean)
    suspend fun saveShowExchangeRate(showExchangeRate: Boolean)
}

class UserPreferencesImpl(
    private val dataStore: DataStore<Preferences>
): UserPreferences {

    private companion object {
        val MONTHLY_BUDGET = doublePreferencesKey("monthly_budget")
        val ALLOW_DAILY_SUMMARY = booleanPreferencesKey("allow_daily_summary")
        val BUDGET_ALERT = booleanPreferencesKey("budget_alert")
        val SHOW_EXCHANGE_RATE = booleanPreferencesKey("show_exchange_rate")
        const val TAG = "UserPreferences"
    }

    override suspend fun saveMonthlyBudget(budget: Double) {
        dataStore.edit { preferences ->
            preferences[MONTHLY_BUDGET] = budget
        }
    }

    override suspend fun saveAllowDailySummary(dailySummary: Boolean) {
        dataStore.edit { preferences ->
            preferences[ALLOW_DAILY_SUMMARY] = dailySummary
        }
    }

    override suspend fun saveAllowBudgetAlert(budgetAlert: Boolean) {
        dataStore.edit { preferences ->
            preferences[BUDGET_ALERT] = budgetAlert
        }
    }

    override suspend fun saveShowExchangeRate(showExchangeRate: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_EXCHANGE_RATE] = showExchangeRate
        }
    }

    override val monthlyBudget: Flow<Double> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading Monthly Budget", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[MONTHLY_BUDGET] ?: 0.0
        }

    override val allowDailySummary: Flow<Boolean> = dataStore.data
        .catch { throwable ->
            if(throwable is IOException) {
                Log.e(TAG,"Error reading Daily Summary", throwable)
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }.map { preferences ->
            preferences[ALLOW_DAILY_SUMMARY] ?: true
        }

    override val budgetAlert: Flow<Boolean> = dataStore.data
        .catch { throwable ->
            if(throwable is IOException) {
                Log.e(TAG,"Error reading Daily Summary", throwable)
                emit(emptyPreferences())
            }else {
                throw throwable
            }
        }.map { preferences ->
            preferences[BUDGET_ALERT] ?: true
        }

    override val showExchangeRate: Flow<Boolean> = dataStore.data
        .catch { throwable ->
            if(throwable is IOException) {
                Log.e(TAG,"Error reading Show Exchange Rate", throwable)
                emit(emptyPreferences())
            }else {
                throw throwable
            }
        }.map { preferences ->
            preferences[SHOW_EXCHANGE_RATE] ?: true
        }

}
