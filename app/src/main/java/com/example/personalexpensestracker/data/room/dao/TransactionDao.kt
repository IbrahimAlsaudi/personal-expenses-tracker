package com.example.personalexpensestracker.data.room.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.personalexpensestracker.data.room.entity.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {

    @Insert
    suspend fun addTransaction(transaction: Transaction)
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("""
        SELECT * FROM `transaction`
        ORDER BY id DESC
    """)
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("""
        SELECT * FROM `transaction` 
        ORDER BY id DESC
        LIMIT(5)
        
    """)
    fun getRecentTransactions(): Flow<List<Transaction>>

    @Query("""
        SELECT * FROM `transaction`
        WHERE category = :query
        OR type = :query
        ORDER BY id DESC
    """)
    fun getTransactionBasedOnTypeOrCategory(query: String): Flow<List<Transaction>>

    @Query("""
        SELECT SUM(amount) FROM `transaction`
        WHERE type = 'INCOME'
    """)
    fun getTotalIncome(): Flow<Double>

    @Query("""
        SELECT SUM(amount) FROM `transaction`
        WHERE type = 'EXPENSE'
        
    """)
    fun getTotalExpense(): Flow<Double>

    @Query("""
        SELECT SUM(amount)
        FROM `transaction`
        WHERE type = 'EXPENSE'
        AND date >= :startOfMonth 
        AND date <= :endOfMonth
    """)
    suspend fun getMonthlyExpenses(startOfMonth: Long, endOfMonth: Long): Double

    @Query("""
        SELECT * FROM `transaction`
        WHERE note 
        LIKE '%' || :note || '%'
        ORDER BY id DESC
    """)
    fun getTransactionByNote(note: String): Flow<List<Transaction>>
}