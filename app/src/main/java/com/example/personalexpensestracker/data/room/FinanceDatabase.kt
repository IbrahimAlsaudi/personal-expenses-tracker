package com.example.personalexpensestracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.personalexpensestracker.data.room.dao.ExchangeRateDao
import com.example.personalexpensestracker.data.room.dao.TransactionDao
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import com.example.personalexpensestracker.data.room.entity.Transaction

@Database(entities = [Transaction::class, ExchangeRate::class], version = 1, exportSchema = false)
@TypeConverters(TransactionConverters::class)
abstract class FinanceDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun exchangeRateDao(): ExchangeRateDao

}