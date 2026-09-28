package kz.zhb.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.zhb.habit.ui.theme.HabitandroidTheme
import kz.zhb.navigation.NavigationHost
import kz.zhb.test.ListKey
import kz.zhb.test.counterEntity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HabitandroidTheme {
                NavigationHost(start = ListKey) { navigator ->
                    counterEntity(navigator)
                }
            }
        }
    }
}
