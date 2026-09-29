package kz.zhb.permissions

import android.Manifest
import android.os.Build

/**
 * Runtime-разрешения приложения. Каждое разрешение нужно объявить в манифесте фичи,
 * которая его использует, иначе система молча откажет и статус станет [PermissionStatus.PermanentlyDenied].
 */
sealed class Permission(
    internal val id: String,
    internal val manifest: List<String>,
    /** Ниже этой версии разрешение не запрашивается и считается выданным. */
    internal val minSdk: Int = Build.VERSION_CODES.BASE,
) {
    /** Напоминания о задачах и намазе. До Android 13 выдано по умолчанию. */
    data object Notifications : Permission(
        id = "notifications",
        manifest = listOf(Manifest.permission.POST_NOTIFICATIONS),
        minSdk = Build.VERSION_CODES.TIRAMISU,
    )

    /** Город для расписания намаза — точная геолокация не нужна. */
    data object Location : Permission(
        id = "location",
        manifest = listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
    )

    internal val isRequired: Boolean get() = Build.VERSION.SDK_INT >= minSdk
}
