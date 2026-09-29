package kz.zhb.tasks.impl.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

/**
 * Координаты для поиска города. Точность не важна, поэтому сначала берём свежую последнюю
 * известную точку, и только если её нет — ждём текущую (до [timeout]).
 * Вызывать только после выдачи Permission.Location. null — геолокация выключена или не определилась.
 */
@SuppressLint("MissingPermission")
internal suspend fun Context.currentLocation(): Location? {
    val manager = getSystemService(LocationManager::class.java) ?: return null
    val providers = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)
        .filter(manager::isProviderEnabled)
    if (providers.isEmpty()) return null

    val lastKnown = providers.mapNotNull(manager::getLastKnownLocation).maxByOrNull { it.time }
    if (lastKnown != null && System.currentTimeMillis() - lastKnown.time < MAX_AGE.inWholeMilliseconds) {
        return lastKnown
    }

    return withTimeoutOrNull(TIMEOUT) {
        suspendCancellableCoroutine { continuation ->
            val signal = CancellationSignal()
            continuation.invokeOnCancellation { signal.cancel() }
            LocationManagerCompat.getCurrentLocation(manager, providers.first(), signal, mainExecutor) {
                continuation.resume(it)
            }
        }
    } ?: lastKnown
}

private val MAX_AGE = 1.hours
private val TIMEOUT = 15.seconds
