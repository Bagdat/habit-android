package kz.zhb.tasks.impl.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.zhb.elm.collectState
import kz.zhb.permissions.Permission
import kz.zhb.permissions.PermissionState
import kz.zhb.permissions.PermissionStatus
import kz.zhb.permissions.rememberPermissionState
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
internal fun TasksScreen(viewModel: TasksViewModel = koinViewModel()) {
    val state by viewModel.collectState()
    val context = LocalContext.current
    val location = rememberPermissionState(Permission.Location)
    var attempt by remember { mutableIntStateOf(0) }

    // Сработает сразу, если доступ уже был, после выдачи в диалоге/настройках и по «Повторить».
    // needsPrayerSchedule защищает от повторной загрузки при повороте экрана.
    LaunchedEffect(location.isGranted, attempt) {
        if (!location.isGranted || !state.needsPrayerSchedule) return@LaunchedEffect
        val point = context.currentLocation()
        viewModel.accept(
            if (point != null) TasksEvents.UI.LoadPrayerSchedulers(point.latitude, point.longitude)
            else TasksEvents.UI.LocationUnavailable
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text("Tasks", style = MaterialTheme.typography.headlineMedium)

        if (!location.isGranted) {
            LocationPermission(location)
        } else {
            PrayerSchedule(state, onRetry = { attempt++ })
        }
    }
}

@Composable
private fun PrayerSchedule(state: TasksState, onRetry: () -> Unit) {
    val day = state.prayerDay
    when {
        state.isLoading -> CircularProgressIndicator()

        day != null -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            state.city?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
            PrayerRow("Фаджр", day.fajr)
            PrayerRow("Восход", day.sunrise)
            PrayerRow("Зухр", day.dhuhr)
            PrayerRow("Аср", day.asr)
            PrayerRow("Магриб", day.maghrib)
            PrayerRow("Иша", day.isha)
        }

        state.error != null -> {
            Text(state.error, textAlign = TextAlign.Center)
            Button(onClick = onRetry) { Text("Повторить") }
        }

        // Пока ищем координаты — до первого события в ViewModel
        else -> CircularProgressIndicator()
    }
}

@Composable
private fun PrayerRow(title: String, time: LocalTime) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title)
        Text(time.format(TimeFormat))
    }
}

private val TimeFormat = DateTimeFormatter.ofPattern("HH:mm")

@Composable
private fun LocationPermission(state: PermissionState) {
    when (val status = state.status) {
        PermissionStatus.Granted -> Unit

        is PermissionStatus.Denied -> {
            if (status.shouldShowRationale) {
                Text(
                    text = "Геолокация нужна, чтобы определить город и показать время намаза",
                    textAlign = TextAlign.Center
                )
            }
            Button(onClick = state::request) { Text(text = "Разрешить геолокацию") }
        }

        PermissionStatus.PermanentlyDenied -> {
            Text(
                text = "Доступ к геолокации запрещён. Включите его в настройках приложения",
                textAlign = TextAlign.Center,
            )
            OutlinedButton(onClick = state::openSettings) { Text("Открыть настройки") }
        }
    }
}
