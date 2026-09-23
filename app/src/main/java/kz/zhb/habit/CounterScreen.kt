package kz.zhb.habit

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.zhb.elm.CollectEffects

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()

    viewModel.CollectEffects { effect ->
        when (effect) {
            is CounterEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "${state.count}", style = MaterialTheme.typography.displayMedium)

        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = { viewModel.accept(CounterEvents.UI.Subtract) }) { Text("−") }
            Spacer(Modifier.width(16.dp))
            Button(onClick = { viewModel.accept(CounterEvents.UI.Add) }) { Text("+") }
        }

        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            Text("Loading...")
        }
    }
}