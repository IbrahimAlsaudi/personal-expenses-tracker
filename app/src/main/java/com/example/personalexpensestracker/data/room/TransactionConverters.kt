package com.example.personalexpensestracker.data.room

import androidx.room.TypeConverter
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType

class TransactionConverters {

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name
    @TypeConverter
    fun toTransactionType(value: String): TransactionType =
        TransactionType.valueOf(value)

    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory): String = category.name

    @TypeConverter
    fun toTransactionCategory(value: String): TransactionCategory =
        TransactionCategory.valueOf(value)
}