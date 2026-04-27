package com.example.personalexpensestracker.data

import android.content.Context
import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.ExchangeRateRepositoryImpl
import com.example.personalexpensestracker.data.repository.OfflineTransactionRepository
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.room.FinanceDatabase
import com.example.personalexpensestracker.network.FinanceApiService
import com.example.personalexpensestracker.network.RetrofitInstance
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val transactionRepository: TransactionRepository
    val exchangeRateRepository: ExchangeRateRepository
}

class AppContainerImpl(context: Context): AppContainer{



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

}