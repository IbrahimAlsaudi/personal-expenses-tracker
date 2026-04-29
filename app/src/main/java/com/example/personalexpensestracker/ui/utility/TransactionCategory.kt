package com.example.personalexpensestracker.ui.utility

enum class TransactionCategory(val uiName: String, val type: TransactionType,) {

    FOOD("Food", TransactionType.EXPENSE),
    TRANSPORT("Transport", TransactionType.EXPENSE),
    SHOPPING("Shopping", TransactionType.EXPENSE),
    HEALTH("Health",TransactionType.EXPENSE),
    ENTERTAINMENT("Entertainment",TransactionType.EXPENSE),
    EXPENSE_OTHER("Other",TransactionType.EXPENSE),

    SALARY("Salary",TransactionType.INCOME),
    FREELANCE("Freelance",TransactionType.INCOME),
    BUSINESS("Business",TransactionType.INCOME),
    GIFT("Gift",TransactionType.INCOME),
    INCOME_OTHER("Other",TransactionType.INCOME)
}