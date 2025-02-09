package snoozeloo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frokanic.snoozeloo.theme.SnoozelooTheme
import dagger.hilt.android.AndroidEntryPoint
import snoozeloo.ringing.AlarmRingingScreen

@AndroidEntryPoint
class AlarmRingingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()

        val alarmTime = intent.getStringExtra("TIME") ?: ""
        val alarmName = intent.getStringExtra("NAME")

        Log.d("Debugging", "The alarmTime is $alarmTime and the alarmName is $alarmName")

        setContent {
            SnoozelooTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "ringing"
                ) {
                    composable("ringing") {
                        // Pass alarmTime and alarmName if your screen needs them.
                        AlarmRingingScreen(
                            time = alarmTime,
                            name = alarmName,
                            onCloseAlarmRingingScreen = {
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}