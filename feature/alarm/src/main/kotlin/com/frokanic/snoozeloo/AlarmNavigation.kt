package com.frokanic.snoozeloo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.frokanic.snoozeloo.alarmdetails.AlarmDetailsScreen
import com.frokanic.snoozeloo.allalarms.AllAlarmsScreen
import kotlinx.serialization.Serializable

@Composable
fun AlarmNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AllAlarms
    ) {
        composable<AllAlarms> {
            AllAlarmsScreen(
                navigateToDetails = { id ->
                    navController
                        .navigate(
                            route = AlarmDetails(
                                id = id
                            )
                        )
                }
            )
        }

        composable<AlarmDetails> {
            AlarmDetailsScreen(
                navigateToAllAlarms = {
                    navController.navigateUp()
                }
            )
        }
    }
}

@Serializable
data object AllAlarms

@Serializable
data class AlarmDetails(
    val id: Int?
)