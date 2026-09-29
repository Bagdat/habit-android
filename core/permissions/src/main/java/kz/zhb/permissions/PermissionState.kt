package kz.zhb.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

/**
 * Состояние разрешения для экрана. Запрос живёт в UI-слое (нужна Activity),
 * во ViewModel уходит только результат — через [onResult] как событие ELM.
 *
 * ```
 * val notifications = rememberPermissionState(Permission.Notifications) { granted ->
 *     viewModel.dispatch(Event.NotificationsPermission(granted))
 * }
 * when (notifications.status) {
 *     PermissionStatus.Granted -> …
 *     is PermissionStatus.Denied -> Button(onClick = notifications::request) { … }
 *     PermissionStatus.PermanentlyDenied -> Button(onClick = notifications::openSettings) { … }
 * }
 * ```
 *
 * Статус обновляется на ON_RESUME — в том числе после возврата из настроек приложения.
 */
@Composable
fun rememberPermissionState(
    permission: Permission,
    onResult: (granted: Boolean) -> Unit = {},
): PermissionState {
    val activity = checkNotNull(LocalActivity.current) { "rememberPermissionState требует Activity" }
    val currentOnResult by rememberUpdatedState(onResult)

    val state = remember(permission, activity) {
        PermissionState(permission, activity, onResult = { currentOnResult(it) })
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        state::onRequestResult,
    )
    DisposableEffect(state, launcher) {
        state.launcher = launcher
        onDispose { state.launcher = null }
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { state.refresh() }

    return state
}

@Stable
class PermissionState internal constructor(
    val permission: Permission,
    private val activity: Activity,
    private val onResult: (Boolean) -> Unit,
) {
    private val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val deniedForeverKey = "denied_forever_${permission.id}"

    internal var launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>? = null

    var status: PermissionStatus by mutableStateOf(currentStatus())
        private set

    val isGranted: Boolean get() = status == PermissionStatus.Granted

    /** Показывает системный диалог. Если разрешение уже выдано — сразу отдаёт onResult(true). */
    fun request() {
        if (isGranted) {
            onResult(true)
            return
        }
        launcher?.launch(permission.manifest.toTypedArray())
    }

    fun openSettings() {
        activity.openAppSettings()
    }

    internal fun refresh() {
        // Выдали через настройки — флаг больше не актуален: после отзыва система снова покажет диалог
        if (hasPermission()) prefs.edit { remove(deniedForeverKey) }
        status = currentStatus()
    }

    internal fun onRequestResult(result: Map<String, Boolean>) {
        val granted = result.isNotEmpty() && result.values.all { it }
        // Отказ без rationale после запроса = «больше не спрашивать» (или второй отказ).
        // Android не отличает это от закрытого без выбора диалога, поэтому помним флагом.
        val deniedForever = !granted && !shouldShowRationale()
        prefs.edit { putBoolean(deniedForeverKey, deniedForever) }
        refresh()
        onResult(granted)
    }

    private fun currentStatus(): PermissionStatus = resolveStatus(
        granted = hasPermission(),
        shouldShowRationale = shouldShowRationale(),
        deniedForever = prefs.getBoolean(deniedForeverKey, false),
    )

    private fun hasPermission(): Boolean =
        !permission.isRequired || permission.manifest.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun shouldShowRationale(): Boolean =
        permission.isRequired && permission.manifest.any(activity::shouldShowRequestPermissionRationale)

    private companion object {
        const val PREFS_NAME = "kz.zhb.permissions"
    }
}

fun Context.openAppSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )
}
