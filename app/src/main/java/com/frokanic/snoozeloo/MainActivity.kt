package com.frokanic.snoozeloo

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.frokanic.snoozeloo.navigation.AppNavigation
import com.frokanic.snoozeloo.theme.SnoozelooTheme
import dagger.hilt.android.AndroidEntryPoint
import android.provider.Settings
import androidx.core.app.ActivityCompat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.USE_FULL_SCREEN_INTENT,
                    android.Manifest.permission.USE_EXACT_ALARM,
                    android.Manifest.permission.SCHEDULE_EXACT_ALARM
                ),
                1
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // This is how you request user permission to schedule exact alarms:
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }

        window.requestFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()

        setContent {
            SnoozelooTheme {
                AppNavigation()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}