package com.example.personalexpensestracker.ui.screens.settings

import androidx.annotation.StringRes
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensestracker.AppViewModelProvider
import com.example.personalexpensestracker.R
import com.example.personalexpensestracker.ui.screens.addtransaction.AmountTextField
import com.example.personalexpensestracker.ui.screens.history.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen (
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(stringResource(R.string.settings))
        }
        item {
            Column {
                Text(stringResource(R.string.preferences))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                    Text(stringResource(R.string.monthly_budget))
                    TextButton(
                        onClick = {viewModel.openBottomSheet()}
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.weight(1f),

                            ) {
                            Text(stringResource(R.string.saved_budget_format,
                                uiState.value.monthlyBudget.ifEmpty { "0.0" }
                            )
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(R.drawable.baseline_arrow_forward_24),
                                contentDescription = "icon"
                            )
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text(stringResource(R.string.notifications))
                SwitchRow(
                    name = R.string.daily_summary,
                    checked = uiState.value.dailySummary,
                    onCheckChanged = {viewModel.onDailySummaryChanged(it)}
                )
                SwitchRow(
                    name = R.string.budget_alert,
                    checked = uiState.value.budgetAlert,
                    onCheckChanged = {viewModel.onBudgetAlertChanged(it)}
                )
            }
        }

        item {
            Column {
                Text(stringResource(R.string.display))
                SwitchRow(
                    name = R.string.show_exchange_rates,
                    checked = uiState.value.showExchangeRate,
                    onCheckChanged = {viewModel.onShowExchangeRateChanged(it)}
                )

            }
        }
        if(uiState.value.showBottomSheetState) {
            item {
                ModalBottomSheet(
                    onDismissRequest = {viewModel.closeBottomSheet()}
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.monthly_budget),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Set your total monthly spending limit to receive budget alerts.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AmountTextField(
                            value = uiState.value.monthlyBudget,
                            onValueChange = { viewModel.onMonthlyBudgetChanged(it) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.closeBottomSheet() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Done")
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun SwitchRow(
    @StringRes name: Int,
    onCheckChanged: (Boolean) -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(name))
        Switch(
            checked = checked,
            onCheckedChange = onCheckChanged
        )
    }
}