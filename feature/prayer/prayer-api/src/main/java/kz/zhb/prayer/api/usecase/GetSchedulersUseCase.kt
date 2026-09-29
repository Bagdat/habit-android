package kz.zhb.prayer.api.usecase

import kotlinx.coroutines.flow.Flow
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.model.PrayerSchedule

interface GetSchedulersUseCase {
    /** lat/lng — любые координаты (например, с GPS): ближайший населённый пункт определяется сам. */
    operator fun invoke(lat: Double, lng: Double): Flow<AsyncResult<PrayerSchedule>>
}
