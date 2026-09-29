package kz.zhb.prayer.api.usecase

import kotlinx.coroutines.flow.Flow
import kz.zhb.network.api.AsyncResult

/**
 * Обновляет годовое расписание в БД. В сеть идёт, только если в БД нет расписания на текущий год
 * или пользователь заметно сместился и ближайший город сменился. Данные читать через [ObservePrayerDayUseCase].
 */
interface SyncPrayerScheduleUseCase {
    /** lat/lng — любые координаты (например, с GPS): ближайший населённый пункт определяется сам. */
    operator fun invoke(lat: Double, lng: Double): Flow<AsyncResult<Unit>>
}
