package kz.zhb.test.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.zhb.elm.CollectEffects
import kz.zhb.elm.collectState

@Composable
internal fun DetailScreen(
    viewModel: DetailViewModel,
    onNext: () -> Unit,
) {
    val state by viewModel.collectState()

    viewModel.CollectEffects { effect ->
        when (effect) {
            DetailEffect.OpenInfo -> onNext()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = state.item.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "id = ${state.item.id}")
        Spacer(Modifier.height(8.dp))
        Text(text = state.item.description)
        Spacer(Modifier.height(8.dp))
        Text(text = state.stats ?: "Загрузка статистики…")
        Spacer(Modifier.height(24.dp))
        Button(
            content = {
                Text(text = "On next")
            },
            onClick = { viewModel.accept(DetailEvents.UI.NextClicked) }
        )
    }
}
