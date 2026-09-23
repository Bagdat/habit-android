package kz.zhb.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
//        Text(text = "${state.count}", style = MaterialTheme.typography.displayMedium)
//
//        Spacer(Modifier.height(16.dp))
//
//        Row {
//            Button(onClick = { viewModel.dispatch(CounterMsg.Decrement) }) { Text("−") }
//            Spacer(Modifier.width(16.dp))
//            Button(onClick = { viewModel.dispatch(CounterMsg.Increment) }) { Text("+") }
//        }
    }
}