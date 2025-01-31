package com.frokanic.snoozeloo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.frokanic.snoozeloo.AlarmNavHost

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    AlarmNavHost(
        navController = navController
    )
}