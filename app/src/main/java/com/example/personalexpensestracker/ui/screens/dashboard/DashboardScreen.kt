package com.example.personalexpensestracker.ui.screens.dashboard


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensestracker.AppViewModelProvider
import com.example.personalexpensestracker.R
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import com.example.personalexpensestracker.ui.components.TransactionRow
import com.example.personalexpensestracker.ui.theme.Gray400
import com.example.personalexpensestracker.ui.theme.Green400
import com.example.personalexpensestracker.ui.theme.Red400
import com.example.personalexpensestracker.ui.utility.toTimeAgo

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.factory),
) {

    val uiState by viewModel.uiState.collectAsState()


    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(stringResource(R.string.dashboard))
        }

        item {
            MyFinanceCard(
                balance = uiState.totalBalance,
                income = uiState.totalIncome,
                expenses = uiState.totalExpenses,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (uiState.showExchangeRates) {
            item {
                ExchangeRatesRow(
                    ratesList = uiState.exchangeRates,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
        item {
            Text(text = stringResource(R.string.recent_transactions))
        }

        items(uiState.recentTransactions) { transaction ->
            TransactionRow(transaction = transaction, modifier = Modifier.fillMaxWidth())
        }
    }
}


@Composable
private fun MyFinanceCard(
    balance: Double,
    income: Double,
    expenses: Double,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.total_balance),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = stringResource(R.string.total_balance_format, balance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
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
    }
}

@Composable
private fun FinanceStat(
    label: String,
    amount: Double,
    isIncome: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = stringResource(
                if (isIncome) R.string.income_format else R.string.expense_format,
                amount
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (isIncome)
                Green400
            else
                Red400
        )
    }
}

@Composable
fun ExchangeRatesRow(
    ratesList: List<ExchangeRate>,
    modifier: Modifier = Modifier

) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {

            Text(
                text = stringResource(R.string.exchange_rates),
                modifier = Modifier.weight(1f)
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                ratesList.forEach { it ->
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = it.currency)
                        Text(text = stringResource(R.string.exchange_rate_format, it.rate))

                    }

                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Last updated " +
                    if (ratesList.isNotEmpty()) ratesList.first().lastUpdated.toTimeAgo() else "",
            style = MaterialTheme.typography.bodySmall,
            color = Gray400

        )
    }
}