package com.example.personalexpensestracker

import androidx.room.util.query
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class FakeTransactionRepository: TransactionRepository {
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())
    fun emit(list: List<Transaction>) {
        transactions.value = list
    }
    override suspend fun addTransaction(transaction: Transaction) {
        transactions.value += transaction
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactions.value -= transaction
    }

    override fun getAllTransactions(): Flow<List<Transaction>> = transactions

    override fun getRecentTransactions(): Flow<List<Transaction>> {
        return  if (transactions.value.size > 5) transactions.map { it.subList(0,5) } else transactions
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> {
        return transactions.map { list -> list.filter { it.type == type } }
    }

    override fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>> {
        return transactions.map { list -> list.filter { it.category == category } }
    }

    override fun getTotalIncome(): Flow<Double> {
        return transactions.map { list ->
            list.filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
        }
    }

    override fun getTotalExpense(): Flow<Double> {
        return transactions.map { list ->
            list.filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
        }
    }

    override suspend fun getMonthlyExpenses(
        startOfMonth: Long,
        endOfMonth: Long
    ): Double {
        val x = transactions.value
            .filter {
                it.type == TransactionType.EXPENSE
            }
            .filter {
                it.date in startOfMonth..endOfMonth
        }.sumOf { it.amount }
        return x
    }

    override fun getTransactionByNote(note: String): Flow<List<Transaction>> {
        return transactions.map { list ->
            list.filter { it.note?.contains(note, ignoreCase = true) ?: false }
        }
    }
}