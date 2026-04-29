package com.example.personalexpensestracker.ui.screens.addtransaction


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.personalexpensestracker.ui.theme.Blue100
import com.example.personalexpensestracker.ui.theme.Blue200
import com.example.personalexpensestracker.ui.theme.Blue400
import com.example.personalexpensestracker.ui.theme.Green100
import com.example.personalexpensestracker.ui.theme.Green400
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import com.example.personalexpensestracker.ui.utility.icon

@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    viewModel: AddTransactionViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.new_transaction))

        TransactionTypeSelector(
            isExpenseSelected = uiState.value.isExpenseSelected,
            onExpenseClick = {
                viewModel.changeType(TransactionType.EXPENSE)
                viewModel.onExpensesSelected()
            },
            onIncomeClick = {
                viewModel.changeType(TransactionType.INCOME)
                viewModel.onIncomeSelected()
            },
            modifier = Modifier.fillMaxWidth()
        )

        AmountTextField(
            value = uiState.value.amount,
            onValueChange = { viewModel.updateAmount(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.category),
            style = MaterialTheme.typography.labelLarge
        )
        AnimatedContent(
            targetState = uiState.value.transactionType,
            transitionSpec = {
                if (targetState == TransactionType.EXPENSE) {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> width } + fadeOut())
                }
            },
            label = "CategoryTransition"
        ) { targetType ->
            CategoriesFlowRow(
                currentTransactionType = targetType,
                selectedCategory = uiState.value.category,
                onCategoryClick = { viewModel.changeCategory(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        AddNoteColumn(
            note = uiState.value.note ?: "",
            onNoteChange = { viewModel.updateNote(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(
            onClick = {
                navigateUp()
                viewModel.saveTransaction()
            },
            enabled = uiState.value.isFormValid,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = stringResource(R.string.save_transaction))
    }
}

@Composable
fun AddNoteColumn(
    note: String,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.note_optional),
            style = MaterialTheme.typography.labelLarge
        )
        TextField(
            value = note,
            onValueChange = onNoteChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.add_note)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_other_admission_24),
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
fun AmountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = TextStyle(
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.add_balance_format),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    )
}

@Composable
fun CategoriesFlowRow(
    currentTransactionType: TransactionType,
    selectedCategory: TransactionCategory?,
    onCategoryClick: (TransactionCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TransactionCategory.entries
            .filter { it.type == currentTransactionType }
            .forEach { category ->
                FilterChip(
                    modifier = Modifier.height(40.dp),
                    selected = selectedCategory == category,
                    onClick = { onCategoryClick(category) },
                    label = { Text(category.uiName) },
                    leadingIcon = {
                        Icon(
                            painter = category.icon(),
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    },
                    shape = MaterialTheme.shapes.large,
                    border = null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Blue200,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }
    }
}

@Composable
fun TransactionTypeSelector(
    isExpenseSelected: Boolean,
    onExpenseClick: () -> Unit,
    onIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expenseColor by animateColorAsState(
        targetValue = if (isExpenseSelected) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.background,
        label = "ExpenseBtnColor"
    )
    val expenseContentColor by animateColorAsState(
        targetValue = if (isExpenseSelected) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onBackground,
        label = "ExpenseContentColor"
    )
    val incomeColor by animateColorAsState(
        targetValue = if (isExpenseSelected) MaterialTheme.colorScheme.background else Green100,
        label = "IncomeBtnColor"
    )
    val incomeContentColor by animateColorAsState(
        targetValue = if (isExpenseSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSecondaryContainer,
        label = "IncomeContentColor"
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Button(
            onClick = onExpenseClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = expenseColor,
                contentColor = expenseContentColor
            ),
        modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.expenses)
            )
        }

        Button(
            onClick = onIncomeClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = incomeColor,
                contentColor = incomeContentColor
            ),
                    modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.income)
            )
        }
    }
}

