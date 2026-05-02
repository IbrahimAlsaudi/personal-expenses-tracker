package com.example.personalexpensestracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.ExchangeRateRepositoryImpl
import com.example.personalexpensestracker.data.repository.OfflineTransactionRepository
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import com.example.personalexpensestracker.data.repository.UserPreferencesImpl
import com.example.personalexpensestracker.data.repository.WorkManagerRepository
import com.example.personalexpensestracker.data.repository.WorkManagerRepositoryImpl
import com.example.personalexpensestracker.data.room.FinanceDatabase
import com.example.personalexpensestracker.network.RetrofitInstance


private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

interface AppContainer {
    val transactionRepository: TransactionRepository
    val exchangeRateRepository: ExchangeRateRepository
    val userPreferences: UserPreferences
    val workManagerRepository: WorkManagerRepository
}

class AppContainerImpl(context: Context) : AppContainer {


    override val transactionRepository: TransactionRepository by lazy {
        OfflineTransactionRepository(
            transactionDao = FinanceDatabase.getDatabase(context).transactionDao()
        )
    }

    override val exchangeRateRepository: ExchangeRateRepository by lazy {
        ExchangeRateRepositoryImpl(
            exchangeRateDao = FinanceDatabase.getDatabase(context).exchangeRateDao(),
            apiService = RetrofitInstance.apiService
        )
    }

    override val userPreferences: UserPreferences by lazy {
        UserPreferencesImpl(context.dataStore)
    }

    override val workManagerRepository: WorkManagerRepository by lazy {
        WorkManagerRepositoryImpl(context)
    }

}