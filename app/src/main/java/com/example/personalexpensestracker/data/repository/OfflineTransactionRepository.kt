package com.example.personalexpensestracker.data.repository

import com.example.personalexpensestracker.data.room.dao.TransactionDao
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction) =
        transactionDao.addTransaction(transaction)

    override suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    override fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    override fun getRecentTransactions(): Flow<List<Transaction>> =
        transactionDao.getRecentTransactions()

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        transactionDao.getTransactionsByType(type)

    override fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategory(category)


    override fun getTotalIncome(): Flow<Double> = transactionDao.getTotalIncome()

    override fun getTotalExpense(): Flow<Double> = transactionDao.getTotalExpense()

    override suspend fun getMonthlyExpenses(startOfMonth: Long, endOfMonth: Long): Double =
        transactionDao.getMonthlyExpenses(startOfMonth, endOfMonth)

    override fun getTransactionByNote(note: String): Flow<List<Transaction>> =
        transactionDao.getTransactionByNote(note)

}