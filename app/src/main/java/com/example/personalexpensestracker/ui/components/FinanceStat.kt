package com.example.personalexpensestracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.personalexpensestracker.R
import com.example.personalexpensestracker.ui.theme.Green400
import com.example.personalexpensestracker.ui.theme.Red400

@Composable
fun FinanceStat(
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