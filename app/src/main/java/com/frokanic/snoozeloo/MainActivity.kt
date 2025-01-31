package com.frokanic.snoozeloo

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.frokanic.snoozeloo.navigation.AppNavigation
import com.frokanic.snoozeloo.theme.SnoozelooTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        enableEdgeToEdge()

        // Todo do I need the below?
//        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
//        insetsController.isAppearanceLightStatusBars = true

//        enableEdgeToEdge()
        setContent {
            SnoozelooTheme {
                AppNavigation()
            }
        }
    }
}