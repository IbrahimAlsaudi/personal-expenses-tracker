package com.example.personalexpensestracker.ui.utility

import androidx.annotation.StringRes
import com.example.personalexpensestracker.R

enum class TransactionCategory(
    @param:StringRes val uiName: Int,
    val type: TransactionType,
) {

    FOOD(R.string.category_food, TransactionType.EXPENSE),
    TRANSPORT(R.string.category_transport, TransactionType.EXPENSE),
    SHOPPING(R.string.category_shopping, TransactionType.EXPENSE),
    HEALTH(R.string.category_health, TransactionType.EXPENSE),
    ENTERTAINMENT(R.string.category_entertainment, TransactionType.EXPENSE),
    EXPENSE_OTHER(R.string.category_other, TransactionType.EXPENSE),

    SALARY(R.string.category_salary, TransactionType.INCOME),
    FREELANCE(R.string.category_freelance, TransactionType.INCOME),
    BUSINESS(R.string.category_business, TransactionType.INCOME),
    GIFT(R.string.category_gift, TransactionType.INCOME),
    INCOME_OTHER(R.string.category_other, TransactionType.INCOME)
}