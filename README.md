# Personal Expenses Tracker 

A modern, full-featured Android application built with **Jetpack Compose** to help users take control of their financial life. This app provides a seamless experience for tracking daily transactions, monitoring budgets, and staying updated with real-time currency exchange rates.

---

> 
##  Tech Stack

*   **UI**: Jetpack Compose (Material 3)
*   **Navigation**: Compose Navigation with a clean bottom bar implementation.
*   **Architecture**: MVVM (Model-View-ViewModel) with a Repository pattern for Clean Architecture.
*   **Database**: **Room** for local data persistence.
*   **Preferences**: **DataStore** for storing user settings and budget info.
*   **Background Tasks**: **WorkManager** for reliable periodic background syncs and notifications.
*   **Networking**: **Retrofit** for fetching real-time exchange rates from external APIs.
*   **Dependency Injection**: Hilt (hilt branch) & Manual injection via `AppContainer` (master branch).
*   **Animations**: Compose Animation API.


##  Screenshots(en-ar)


<img width="300" height="700" alt="Screenshot_20260502_175949" src="https://github.com/user-attachments/assets/ab2e472e-b8dc-42f0-9640-a41b3ade0879" />

---

<img width="300" height="700" alt="Screenshot_20260502_174100" src="https://github.com/user-attachments/assets/e22c4e47-4675-454a-8ace-bf7af8db3e62" />

---

<img width="300" height="700" alt="Screenshot_20260502_173159" src="https://github.com/user-attachments/assets/0048772b-4a08-475f-a21d-6d315e52bd0b" />

---

<img width="300" height="700" alt="Screenshot_20260502_173144" src="https://github.com/user-attachments/assets/617b5aaa-2087-4f80-a755-ad261567d13d" />

---

<img width="300" height="700" alt="Screenshot_20260502_180101" src="https://github.com/user-attachments/assets/fad28eb6-c373-4253-9eb1-c019aab47367" />

---

<img width="300" height="700" alt="Screenshot_20260502_180135" src="https://github.com/user-attachments/assets/69947f58-cc11-413e-8786-4206bb7aa253" />

---

<img width="200" height="200" alt="WhatsApp Image 2026-05-02 at 6 07 05 PM" src="https://github.com/user-attachments/assets/82bee57f-8218-44ba-97d1-caa9d2144cd7" />


##  Key Features


### 1. Smart Dashboard 📊
*   **Real-time Balance**: Instantly view your total balance, total income, and total expenses.
*   **Recent Activity**: Quick access to your most recent transactions directly on the home screen.
*   **Live Exchange Rates**: Stay informed with background-synced EGP exchange rates.

### 2. Effortless Transaction Logging ➕
*   **Intuitive UI**: A clean interface designed for speed, featuring a large, centered numeric input for amounts.
*   **Animated Categories**: Smart category selection that changes dynamically based on whether you are logging an Expense or Income.
*   **Smooth Transitions**: Directional animations for a polished feel.
*   **Notes & Metadata**: Add optional notes to keep track of specific details.

### 3. Comprehensive History & Search 🔍
*   **Advanced Filtering**: Filter your financial history by Transaction Type (Income/Expense) or specific Category (Food, Salary, Shopping, etc.).
*   **Search Functionality**: Quickly find specific transactions by searching through your notes (Works side by side with the filtering).
*   **Swipe-to-delete**: Manage your records easily with intuitive swipe-to-delete gestures on transaction items.
*   **Dynamic Totals**: See the total income and expenses for your filtered results in real-time.

### 4. Intelligent Budgeting & Notifications 🔔
*   **Monthly Budgeting**: Set a monthly spending limit via a sleek Modal Bottom Sheet.
*   **Smart Alerts**: Receive immediate notifications when you reach 90% of your budget or exceed your limit.
*   **Daily Summaries**: Get a recap of your daily spending every evening to stay mindful of your habits.

### 5. Multi-Language & RTL Support 🌍
*   **Full Arabic Support**: A completely localized experience, including Right-to-Left (RTL) layout mirroring.
*   **Natural Language**: Uses advanced plural logic (Quantity Strings) to show human-readable dates correctly.

---

## Testing
Unit tests written for `HistoryViewModel` and `DashboardViewModel` using:
- **JUnit4** — test framework
- **Turbine** — Flow testing
- **kotlinx-coroutines-test** — coroutine dispatcher control

Coverage includes: search filtering, transaction type/category filtering, 
combine + flatMapLatest toggle behavior, and balance calculation.

---

## 📐 Project Structure

*   `data/`: Contains the Room database, DAOs, Entities, and Repository implementations.
*   `ui/`:
    *   `screens/`: Individual screen implementations (Dashboard, Add Transaction, History, Settings) and view models.
    *   `components/`: Reusable UI elements (TransactionRows, TextFields, Stats).
    *   `theme/`: Material 3 color schemes, typography, and shapes.
*   `worker/`: Background workers for notifications and API updates.
*   `utility/`: Helper functions, extension functions for localization, and enums.

    
