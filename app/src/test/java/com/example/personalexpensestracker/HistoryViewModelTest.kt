package com.example.personalexpensestracker

import app.cash.turbine.test
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.screens.history.HistoryViewModel
import com.example.personalexpensestracker.ui.screens.history.TransactionFilter
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeTransactionRepository
    private lateinit var viewModel: HistoryViewModel

    private val incomeTransaction = Transaction(
        id = 1,
        amount = 500.0,
        type = TransactionType.INCOME,
        category = TransactionCategory.SALARY,
        note = "Monthly salary",
        date = System.currentTimeMillis()
    )

    private val expenseTransaction = Transaction(
        id = 2,
        amount = 100.0,
        type = TransactionType.EXPENSE,
        category = TransactionCategory.FOOD,
        note = "Grocery shopping",
        date = System.currentTimeMillis()
    )

    @Before
    fun setup() {
        fakeRepository = FakeTransactionRepository()
        viewModel = HistoryViewModel(fakeRepository)
    }

    // --- Initial State ---

    @Test
    fun `initial state has empty transactions`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.transactions.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state has zero income and expense`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(0.0, state.totalIncome, 0.01)
            assertEquals(0.0, state.totalExpense, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial filter is None`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(TransactionFilter.None, state.transactionFilter)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Transactions Loading ---

    @Test
    fun `loading transactions shows all transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.transactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Totals ---

    @Test
    fun `total income is calculated correctly`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(500.0, state.totalIncome, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `total expense is calculated correctly`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(100.0, state.totalExpense, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Search ---

    @Test
    fun `search by note returns matching transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onQueryChanged("salary")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.transactions.size)
            assertEquals("Monthly salary", state.transactions.first().note)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search with empty query returns all transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onQueryChanged("")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.transactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search with no match returns empty list`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onQueryChanged("zzzzz")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.transactions.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Filter by Type ---

    @Test
    fun `filter by income type returns only income transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onFilterChanged(TransactionFilter.ByType(TransactionType.INCOME))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.transactions.size)
            assertTrue(state.transactions.all { it.type == TransactionType.INCOME })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filter by expense type returns only expense transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onFilterChanged(TransactionFilter.ByType(TransactionType.EXPENSE))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.transactions.size)
            assertTrue(state.transactions.all { it.type == TransactionType.EXPENSE })
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Filter by Category ---

    @Test
    fun `filter by category returns only matching transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onFilterChanged(TransactionFilter.ByCategory(TransactionCategory.FOOD))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.transactions.size)
            assertEquals(TransactionCategory.FOOD, state.transactions.first().category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filter by category with no match returns empty list`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onFilterChanged(TransactionFilter.ByCategory(TransactionCategory.ENTERTAINMENT))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.transactions.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Filter None ---

    @Test
    fun `filter None returns all transactions`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onFilterChanged(TransactionFilter.ByType(TransactionType.INCOME))
        viewModel.onFilterChanged(TransactionFilter.None)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.transactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Search + Filter combined ---

    @Test
    fun `search and filter work together`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onQueryChanged("grocery")
        viewModel.onFilterChanged(TransactionFilter.ByType(TransactionType.EXPENSE))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.transactions.size)
            assertEquals("Grocery shopping", state.transactions.first().note)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search matches but filter excludes returns empty list`() = runTest {
        fakeRepository.emit(listOf(incomeTransaction, expenseTransaction))
        viewModel.onQueryChanged("grocery")
        viewModel.onFilterChanged(TransactionFilter.ByType(TransactionType.INCOME))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.transactions.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}