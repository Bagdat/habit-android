package kz.zhb.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.zhb.habit.ui.theme.HabitandroidTheme
import kz.zhb.main.impl.mainEntry
import kz.zhb.navigation.NavigationHost
import kz.zhb.onboarding.impl.onboardingEntry
import kz.zhb.splash.api.SplashKey
import kz.zhb.splash.impl.splashEntry

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HabitandroidTheme {
                NavigationHost(start = SplashKey) { navigator ->
                    splashEntry(navigator)
                    onboardingEntry(navigator)
                    mainEntry(navigator)
                }
            }
        }
    }
}
