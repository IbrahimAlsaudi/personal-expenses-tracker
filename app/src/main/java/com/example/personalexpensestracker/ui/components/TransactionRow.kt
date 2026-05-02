package com.example.personalexpensestracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.personalexpensestracker.R
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.theme.Green400
import com.example.personalexpensestracker.ui.theme.Red400
import com.example.personalexpensestracker.ui.utility.TransactionType
import com.example.personalexpensestracker.ui.utility.icon
import com.example.personalexpensestracker.ui.utility.toTimeAgo

@Composable
fun TransactionRow(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            transaction.category.icon(),
            contentDescription = "icon"
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(
                    text = stringResource(transaction.category.uiName),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                if (transaction.note != null) {
                    Text(
                        text = "  ${transaction.note}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            Text(
                text = transaction.date.toTimeAgo(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = stringResource(
                if (transaction.type == TransactionType.INCOME) R.string.income_format else R.string.expense_format,
                transaction.amount
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.type == TransactionType.INCOME)
                Green400
            else
                Red400,
//            modifier = Modifier.weight(1f)
        )
    }
}