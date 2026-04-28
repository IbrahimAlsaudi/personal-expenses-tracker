package com.example.personalexpensestracker.data.repository

import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getRecentTransactions(): Flow<List<Transaction>>
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>>
    fun getTotalIncome(): Flow<Double>
    fun getTotalExpense(): Flow<Double>
    suspend fun getMonthlyExpenses(startOfMonth: Long, endOfMonth: Long): Double
    fun getTransactionByNote(note: String): Flow<List<Transaction>>
}