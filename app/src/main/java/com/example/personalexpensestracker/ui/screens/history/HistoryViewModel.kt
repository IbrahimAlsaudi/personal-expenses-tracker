package com.example.personalexpensestracker.ui.screens.history


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {
    private val income = transactionRepository.getTotalIncome()
    private val expenses = transactionRepository.getTotalExpense()

    private val searchQuery = MutableStateFlow("")
    fun onQueryChanged(newQuery: String) {
        searchQuery.value = newQuery

    }

    private val filter = MutableStateFlow<TransactionFilter>(TransactionFilter.None)
    fun onFilterChanged(transactionFilter: TransactionFilter) {
        filter.value = transactionFilter

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val transactions = combine(searchQuery, filter) { query, filter ->
        Pair(query, filter)
    }.flatMapLatest { (query, filter) ->
        when {
            query.isEmpty() && filter is TransactionFilter.None ->
                transactionRepository.getAllTransactions()

            query.isNotEmpty() && filter is TransactionFilter.None ->
                transactionRepository.getTransactionByNote(query)

            query.isEmpty() && filter is TransactionFilter.ByType ->
                transactionRepository.getTransactionsByType(filter.type)

            query.isEmpty() && filter is TransactionFilter.ByCategory ->
                transactionRepository.getTransactionsByCategory(filter.category)

            query.isNotEmpty() && filter is TransactionFilter.ByType ->
                transactionRepository.getTransactionsByType(filter.type)
                    .map { list ->
                        list.filter {
                            it.note?.contains(query, ignoreCase = true) == true
                        }
                    }
            query.isNotEmpty() && filter is TransactionFilter.ByCategory ->
                transactionRepository.getTransactionsByCategory(filter.category)
                    .map { list ->
                        list.filter {
                            it.note?.contains(query, ignoreCase = true) == true
                        }

                    }

            else -> transactionRepository.getAllTransactions()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }

    val uiState = combine(income, expenses, searchQuery,
        filter, transactions) {income, expenses, query, filter, transaction ->
        HistoryUiState(
            totalIncome = income,
            totalExpense = expenses,
            searchByNote = query,
            transactionFilter = filter,
            transactions = transaction
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState()
    )
}

data class HistoryUiState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val searchByNote: String = "",
    val transactionFilter: TransactionFilter = TransactionFilter.None,
    val transactions: List<Transaction> = emptyList()
)
sealed class TransactionFilter {
    object None: TransactionFilter()
    data class ByType(val type: TransactionType): TransactionFilter()
    data class ByCategory(val category: TransactionCategory): TransactionFilter()
}