package kz.zhb.splash.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.zhb.elm.CollectEffects

@Composable
internal fun SplashScreen(
    onFinished: () -> Unit,
    viewModel: SplashViewModel = viewModel(),
) {
    viewModel.CollectEffects { effect ->
        when (effect) {
            SplashEffect.OpenOnboarding -> onFinished()
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Habit", style = MaterialTheme.typography.displayMedium)
    }
}
