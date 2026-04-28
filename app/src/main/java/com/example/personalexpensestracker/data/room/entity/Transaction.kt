package com.example.personalexpensestracker.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType

@Entity(
    tableName = "transaction",
    indices = [
        Index(value = ["type"]),
        Index(value = ["category"]),
        Index(value = ["date"])
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: TransactionCategory,
    val type: TransactionType,
    val note: String? = null,
    val date: Long
)

