package com.example.personalexpensestracker

import android.app.Application
import androidx.work.Configuration

import com.example.personalexpensestracker.data.AppContainer
import com.example.personalexpensestracker.data.AppContainerImpl
import com.example.personalexpensestracker.worker.FinanceWorkersFactory

class FinanceApplication: Application(), Configuration.Provider {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
        container.workManagerRepository.scheduleDailySummary()
        container.workManagerRepository.scheduleExchangeRateRefresh()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                FinanceWorkersFactory(
                    container.transactionRepository,
                    container.userPreferences,
                    container.exchangeRateRepository,
                )
            )
            .build()
}