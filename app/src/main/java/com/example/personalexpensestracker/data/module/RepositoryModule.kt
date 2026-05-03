package com.example.personalexpensestracker.data.module

import com.example.personalexpensestracker.data.repository.ExchangeRateRepository
import com.example.personalexpensestracker.data.repository.ExchangeRateRepositoryImpl
import com.example.personalexpensestracker.data.repository.OfflineTransactionRepository
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.UserPreferences
import com.example.personalexpensestracker.data.repository.UserPreferencesImpl
import com.example.personalexpensestracker.data.repository.WorkManagerRepository
import com.example.personalexpensestracker.data.repository.WorkManagerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: OfflineTransactionRepository): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferences(impl: UserPreferencesImpl): UserPreferences

    @Binds
    @Singleton
    abstract fun bindWorkManagerRepository(impl: WorkManagerRepositoryImpl): WorkManagerRepository
}