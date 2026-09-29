package kz.zhb.tasks.impl.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.zhb.permissions.Permission
import kz.zhb.permissions.PermissionState
import kz.zhb.permissions.PermissionStatus
import kz.zhb.permissions.rememberPermissionState

@Composable
fun TasksScreen() {
    val location = rememberPermissionState(Permission.Location)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text("Tasks", style = MaterialTheme.typography.headlineMedium)
        LocationPermission(location)
    }
}

@Composable
private fun LocationPermission(state: PermissionState) {
    when (val status = state.status) {
        PermissionStatus.Granted -> Text("Геолокация доступна")

        is PermissionStatus.Denied -> {
            if (status.shouldShowRationale) {
                Text(
                    "Геолокация нужна, чтобы определить город и показать время намаза",
                    textAlign = TextAlign.Center,
                )
            }
            Button(onClick = state::request) { Text("Разрешить геолокацию") }
        }

        PermissionStatus.PermanentlyDenied -> {
            Text(
                "Доступ к геолокации запрещён. Включите его в настройках приложения",
                textAlign = TextAlign.Center,
            )
            OutlinedButton(onClick = state::openSettings) { Text("Открыть настройки") }
        }
    }
}
