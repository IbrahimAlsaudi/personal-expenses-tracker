package com.example.personalexpensestracker.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRate(
    @PrimaryKey
    val currency: String,
    val rate: Double,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long
)