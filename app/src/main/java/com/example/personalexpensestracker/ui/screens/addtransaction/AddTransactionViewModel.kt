package com.example.personalexpensestracker.ui.screens.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.personalexpensestracker.data.repository.TransactionRepository
import com.example.personalexpensestracker.data.repository.WorkManagerRepository
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val workManagerRepository: WorkManagerRepository
): ViewModel() {

    private val _isExpenseSelected = MutableStateFlow(true)
    fun onIncomeSelected() {
        _isExpenseSelected.value = false
    }
    fun onExpensesSelected() {
        _isExpenseSelected.value = true
    }
    private val _type = MutableStateFlow<TransactionType>(TransactionType.EXPENSE)

    fun changeType(transactionType: TransactionType) {
        _type.value = transactionType
        _category.value = null
    }


    private val _amount = MutableStateFlow("")
    fun updateAmount(amount: String){
        _amount.value = amount
    }
    private val _category = MutableStateFlow<TransactionCategory?>(null)
    fun changeCategory(category: TransactionCategory) {
        _category.value = category
    }

    private val _note = MutableStateFlow<String?>(null)
    fun updateNote(note: String?) {
        _note.value = note
    }



     fun saveTransaction() {
         val category = _category.value ?: return
         val amount = _amount.value.toDoubleOrNull() ?: return
         viewModelScope.launch {
             val transaction = Transaction(
                 amount = amount,
                 category = category,
                 type = _type.value,
                 note = _note.value,
                 date = System.currentTimeMillis()
             )
             transactionRepository.addTransaction(transaction)

             if (transaction.type == TransactionType.EXPENSE) {
                 workManagerRepository.scheduleBudgetAlert()
             }

         }
    }

    val uiState = combine(
        _type,
        _amount,
        _category,
        _note,
        _isExpenseSelected,
//        _isIncomeSelected
    ) { type, amount, category, note, isExpenseSelected ->
        AddTransactionUiState(
            transactionType = type,
            amount = amount,
            category = category,
            note = note,
            isFormValid = amount.isNotEmpty() && category != null,
            isExpenseSelected = isExpenseSelected,

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddTransactionUiState()
    )
}

data class AddTransactionUiState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val category: TransactionCategory? = null,
    val note: String? = null,
    val date: Long = System.currentTimeMillis(),
    val isFormValid: Boolean = false,
    val isExpenseSelected: Boolean = true,
)