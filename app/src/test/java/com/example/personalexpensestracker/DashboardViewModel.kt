package com.example.personalexpensestracker

import app.cash.turbine.test
import com.example.personalexpensestracker.data.room.entity.ExchangeRate
import com.example.personalexpensestracker.data.room.entity.Transaction
import com.example.personalexpensestracker.ui.screens.dashboard.DashboardViewModel
import com.example.personalexpensestracker.ui.utility.TransactionCategory
import com.example.personalexpensestracker.ui.utility.TransactionType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeTransactionRepository: FakeTransactionRepository
    private lateinit var fakeExchangeRateRepository: FakeExchangeRateRepository
    private lateinit var fakeUserPreferences: FakeUserPreferences
    private lateinit var viewModel: DashboardViewModel

    // Sample data
    private val incomeTransaction = Transaction(
        id = 1,
        amount = 1000.0,
        type = TransactionType.INCOME,
        category = TransactionCategory.SALARY,
        note = "Salary",
        date = System.currentTimeMillis()
    )

    private val expenseTransaction = Transaction(
        id = 2,
        amount = 300.0,
        type = TransactionType.EXPENSE,
        category = TransactionCategory.FOOD,
        note = "Groceries",
        date = System.currentTimeMillis()
    )

    private val sampleRates = listOf(
        ExchangeRate(currency = "USD", rate = 30.0, lastUpdated = System.currentTimeMillis()),
        ExchangeRate(currency = "EUR", rate = 33.0, lastUpdated = System.currentTimeMillis())
    )

    @Before
    fun setup() {
        fakeTransactionRepository = FakeTransactionRepository()
        fakeExchangeRateRepository = FakeExchangeRateRepository()
        fakeUserPreferences = FakeUserPreferences()
        viewModel = DashboardViewModel(
            fakeTransactionRepository,
            fakeExchangeRateRepository,
            fakeUserPreferences
        )
    }

    // --- Initial State ---

    @Test
    fun `initial state has correct defaults`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(0.0, state.totalIncome, 0.01)
            assertEquals(0.0, state.totalExpenses, 0.01)
            assertEquals(0.0, state.totalBalance, 0.01)
            assertTrue(state.recentTransactions.isEmpty())
            assertTrue(state.exchangeRates.isEmpty())
            assertTrue(state.showExchangeRates)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Balance ---

    @Test
    fun `balance is totalIncome minus totalExpenses`() = runTest {
        fakeTransactionRepository.emit(listOf(incomeTransaction, expenseTransaction))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1000.0, state.totalIncome, 0.01)
            assertEquals(300.0, state.totalExpenses, 0.01)
            assertEquals(700.0, state.totalBalance, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `balance is negative when expenses exceed income`() = runTest {
        val bigExpense = expenseTransaction.copy(id = 3, amount = 2000.0)
        fakeTransactionRepository.emit(listOf(incomeTransaction, bigExpense))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(-1000.0, state.totalBalance, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `balance is zero when no transactions`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(0.0, state.totalBalance, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Recent Transactions ---

    @Test
    fun `recent transactions shows up to 5 transactions`() = runTest {
        val sixTransactions = (1..6).map {
            incomeTransaction.copy(id = it, amount = it * 100.0)
        }
        fakeTransactionRepository.emit(sixTransactions)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.recentTransactions.size <= 5)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `recent transactions shows all when 5 or fewer`() = runTest {
        val threeTransactions = (1..3).map {
            incomeTransaction.copy(id = it, amount = it * 100.0)
        }
        fakeTransactionRepository.emit(threeTransactions)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(3, state.recentTransactions.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Exchange Rates ---

    @Test
    fun `exchange rates are shown when showExchangeRates is true`() = runTest {
        fakeUserPreferences.setShowExchangeRate(true)
        fakeExchangeRateRepository.emit(sampleRates)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.exchangeRates.size)
            assertTrue(state.showExchangeRates)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `exchange rates are empty when showExchangeRates is false`() = runTest {
        fakeUserPreferences.setShowExchangeRate(false)
        fakeExchangeRateRepository.emit(sampleRates)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.exchangeRates.isEmpty())
            assertFalse(state.showExchangeRates)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggling showExchangeRates off clears exchange rates`() = runTest {
        fakeUserPreferences.setShowExchangeRate(true)
        fakeExchangeRateRepository.emit(sampleRates)

        viewModel.uiState.test {
            // First emission — rates visible
            val stateOn = awaitItem()
            assertEquals(2, stateOn.exchangeRates.size)

            // Toggle off
            fakeUserPreferences.setShowExchangeRate(false)

            // Second emission — rates gone
            val stateOff = awaitItem()
            assertTrue(stateOff.exchangeRates.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggling showExchangeRates on restores exchange rates`() = runTest {
        fakeUserPreferences.setShowExchangeRate(false)
        fakeExchangeRateRepository.emit(sampleRates)

        viewModel.uiState.test {
            // First emission — rates hidden
            val stateOff = awaitItem()
            assertTrue(stateOff.exchangeRates.isEmpty())

            // Toggle on
            fakeUserPreferences.setShowExchangeRate(true)

            // Second emission — rates back
            val stateOn = awaitItem()
            assertEquals(2, stateOn.exchangeRates.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}