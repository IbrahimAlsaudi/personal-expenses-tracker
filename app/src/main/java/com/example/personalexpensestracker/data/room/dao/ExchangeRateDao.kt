package com.example.personalexpensestracker.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import kotlinx.coroutines.flow.Flow


@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(rates: List<ExchangeRate>)

    @Query("SELECT * FROM exchange_rate")
    fun getExchangeRates(): Flow<List<ExchangeRate>>
}