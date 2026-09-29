package kz.zhb.prayer.impl.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.usecase.SyncPrayerScheduleUseCase
import kz.zhb.prayer.impl.local.PrayerLocalDataSource
import kz.zhb.prayer.impl.local.SavedPrayerLocation
import kz.zhb.prayer.impl.repository.PrayerRepository
import java.time.LocalDate

/**
 * 1. Тот же год и пользователь в пределах [SAME_PLACE_KM] от прошлой точки — сеть не нужна.
 * 2. Иначе ищем ближайший город. Тот же город и год — только запоминаем новую точку.
 * 3. Сменился город или наступил новый год — качаем расписание на год и заменяем им БД.
 *
 * muftyat.kz отдаёт расписание только для координат из своего справочника,
 * поэтому расписание берём по координатам города, а не по GPS.
 */
internal class SyncPrayerScheduleUseCaseImpl(
    private val repository: PrayerRepository,
    private val local: PrayerLocalDataSource,
    private val today: () -> LocalDate = LocalDate::now,
) : SyncPrayerScheduleUseCase {

    override fun invoke(lat: Double, lng: Double): Flow<AsyncResult<Unit>> = flow {
        val year = today().year
        val saved = local.savedLocation()?.takeIf { it.year == year }

        if (saved != null && distanceKm(saved.requestLat, saved.requestLng, lat, lng) < SAME_PLACE_KM) {
            emit(AsyncResult.Success(Unit))
            return@flow
        }

        val city = when (val cities = repository.getNearestCities(lat, lng).first()) {
            is AsyncResult.Failure -> return@flow emit(cities)
            is AsyncResult.Success -> cities.data.firstOrNull()
                ?: return@flow emit(AsyncResult.Failure("Рядом нет населённого пункта из справочника"))
        }
        val location = SavedPrayerLocation(city.id, city.title, lat, lng, year)

        if (saved != null && saved.cityId == city.id) {
            local.saveLocation(location)
            emit(AsyncResult.Success(Unit))
            return@flow
        }

        when (val schedule = repository.getSchedulers(year, city.lat, city.lng).first()) {
            is AsyncResult.Failure -> emit(schedule)
            is AsyncResult.Success -> {
                local.replaceSchedule(location, schedule.data)
                emit(AsyncResult.Success(Unit))
            }
        }
    }

    private companion object {
        /** Ближе этого расстояния ближайший город из справочника практически не меняется. */
        const val SAME_PLACE_KM = 3.0
    }
}
