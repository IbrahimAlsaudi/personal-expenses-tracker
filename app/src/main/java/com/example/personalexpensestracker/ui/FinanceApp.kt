package com.example.personalexpensestracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.personalexpensestracker.ui.navigation.Destination
import com.example.personalexpensestracker.ui.navigation.navItemList
import com.example.personalexpensestracker.ui.screens.addtransaction.AddTransactionScreen
import com.example.personalexpensestracker.ui.screens.dashboard.DashboardScreen

@Composable
fun FinanceApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEach {destination ->
                    NavigationBarItem(
                        selected =  currentScreen == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = "icon"
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Destination.Dashboard.route,
            Modifier.padding(innerPadding)
        ) {
            composable(route = Destination.Dashboard.route) {
                DashboardScreen()
            }
            composable(route = Destination.AddTransaction.route) {
                AddTransactionScreen(navigateUp = { navController.navigateUp() })
            }
            composable(route = Destination.History.route) {


            }
            composable(route = Destination.Settings.route) {


            }
        }
    }
}