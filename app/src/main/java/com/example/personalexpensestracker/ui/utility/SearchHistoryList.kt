package com.example.personalexpensestracker.ui.utility

import com.example.personalexpensestracker.ui.screens.history.TransactionFilter


val chips: List<TransactionFilter> = buildList {
    add(TransactionFilter.None)
    TransactionType.entries.forEach { add(TransactionFilter.ByType(it))}
    TransactionCategory.entries.forEach {
        if(!it.name.contains("OTHER"))
            add(TransactionFilter.ByCategory(it))
    }
}

