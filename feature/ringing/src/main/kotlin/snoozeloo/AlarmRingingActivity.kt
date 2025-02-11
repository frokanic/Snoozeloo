package snoozeloo

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frokanic.snoozeloo.theme.SnoozelooTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import snoozeloo.ringing.AlarmRingingScreen

@AndroidEntryPoint
class AlarmRingingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()

        val id = intent.getIntExtra("ID", -1)

        setContent {
            SnoozelooTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AlarmRinging(id = id)
                ) {
                    composable<AlarmRinging> {
                        AlarmRingingScreen(
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

@Serializable
data class AlarmRinging(
    val id: Int?
)