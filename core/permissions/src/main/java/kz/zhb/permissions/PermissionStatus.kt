package kz.zhb.permissions

sealed interface PermissionStatus {
    data object Granted : PermissionStatus

    /**
     * Можно запросить системным диалогом.
     * [shouldShowRationale] = true — пользователь уже отказывал, перед запросом стоит объяснить зачем.
     */
    data class Denied(val shouldShowRationale: Boolean) : PermissionStatus

    /** Системный диалог больше не появится — только через настройки приложения. */
    data object PermanentlyDenied : PermissionStatus
}

internal fun resolveStatus(
    granted: Boolean,
    shouldShowRationale: Boolean,
    deniedForever: Boolean,
): PermissionStatus = when {
    granted -> PermissionStatus.Granted
    shouldShowRationale -> PermissionStatus.Denied(shouldShowRationale = true)
    deniedForever -> PermissionStatus.PermanentlyDenied
    else -> PermissionStatus.Denied(shouldShowRationale = false)
}
