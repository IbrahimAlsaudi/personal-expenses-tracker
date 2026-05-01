package com.example.personalexpensestracker.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensestracker.AppViewModelProvider
import com.example.personalexpensestracker.R
import com.example.personalexpensestracker.ui.components.FinanceStat
import com.example.personalexpensestracker.ui.components.NoteTextField
import com.example.personalexpensestracker.ui.components.TransactionRow
import com.example.personalexpensestracker.ui.utility.chips


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val uiState =viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text(stringResource(R.string.history))
        }
        item {
            TransactionRow(
                uiState.value.totalIncome,
                uiState.value.totalExpense
            )
        }
        item {
            NoteTextField(
                value = uiState.value.searchByNote,
                onValueChange = {viewModel.onQueryChanged(it)},
                placeHolder = R.string.search_transaction,
                )
        }
        item {
            FilterLazyRow(
                selectedFilter = uiState.value.transactionFilter,
                onFilterChanged = { viewModel.onFilterChanged(it) }
            )
        }
        items(uiState.value.transactions){
            TransactionRow(it)
        }
    }
}

@Composable
fun FilterLazyRow(
    selectedFilter: TransactionFilter,
    onFilterChanged: (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips) {
            FilterChip(
                selected = selectedFilter == it,
                onClick = { onFilterChanged(it) },
                label = {
                    Text(
                        when (it) {
                            is TransactionFilter.None -> "All"
                            is TransactionFilter.ByType -> it.type.uiName
                            is TransactionFilter.ByCategory -> it.category.uiName
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun TransactionRow(
    income: Double,
    expenses: Double,
    modifier: Modifier = Modifier){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        FinanceStat(
            label = stringResource(R.string.income),
            amount = income,
            isIncome = true
        )
        FinanceStat(
            label = stringResource(R.string.expenses),
            amount = expenses,
            isIncome = false
        )
    }
}
