package com.example.personalexpensestracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.personalexpensestracker.data.room.dao.ExchangeRateDao
import com.example.personalexpensestracker.data.room.dao.TransactionDao
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import com.example.personalexpensestracker.data.room.entity.Transaction

@Database(entities = [Transaction::class, ExchangeRate::class], version = 1, exportSchema = false)
abstract class FinanceDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun exchangeRateDao(): ExchangeRateDao

    companion object {
        @Volatile
        private var instance: FinanceDatabase? = null
        fun getDatabase(context: Context): FinanceDatabase {
            return instance ?: synchronized(this){
                Room.databaseBuilder(context, FinanceDatabase::class.java, "finance_database")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { instance = it }
            }
        }
    }
}