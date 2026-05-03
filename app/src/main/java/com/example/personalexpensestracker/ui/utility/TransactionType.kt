package com.example.personalexpensestracker.ui.utility

import androidx.annotation.StringRes
import com.example.personalexpensestracker.R

enum class TransactionType(@param:StringRes val uiName: Int) {
    EXPENSE(R.string.type_expense),
    INCOME(R.string.type_income)
}