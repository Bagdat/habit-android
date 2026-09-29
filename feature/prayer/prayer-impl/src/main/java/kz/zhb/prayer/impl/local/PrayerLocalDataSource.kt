package kz.zhb.prayer.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kz.zhb.database.prayer.PrayerDao
import kz.zhb.database.prayer.PrayerLocationEntity
import kz.zhb.database.prayer.PrayerTimeEntity
import kz.zhb.prayer.api.model.PrayerDay
import kz.zhb.prayer.api.model.PrayerDaySchedule
import java.time.LocalDate

/** Для какого места и года в БД лежит расписание. */
internal data class SavedPrayerLocation(
    val cityId: Long,
    val cityTitle: String,
    val requestLat: Double,
    val requestLng: Double,
    val year: Int,
)

internal interface PrayerLocalDataSource {
    suspend fun savedLocation(): SavedPrayerLocation?
    suspend fun saveLocation(location: SavedPrayerLocation)
    suspend fun replaceSchedule(location: SavedPrayerLocation, days: List<PrayerDay>)
    fun observeDay(date: LocalDate): Flow<PrayerDaySchedule?>
}

internal class PrayerLocalDataSourceImpl(private val dao: PrayerDao) : PrayerLocalDataSource {

    override suspend fun savedLocation(): SavedPrayerLocation? = dao.location()?.toDomain()

    override suspend fun saveLocation(location: SavedPrayerLocation) {
        dao.upsertLocation(location.toEntity())
    }

    override suspend fun replaceSchedule(location: SavedPrayerLocation, days: List<PrayerDay>) {
        dao.replaceSchedule(location.toEntity(), days.map { it.toEntity() })
    }

    override fun observeDay(date: LocalDate): Flow<PrayerDaySchedule?> =
        combine(dao.observeDay(date), dao.observeLocation()) { day, location ->
            if (day == null || location == null) null
            else PrayerDaySchedule(city = location.cityTitle, day = day.toDomain())
        }
}

private fun PrayerLocationEntity.toDomain() = SavedPrayerLocation(cityId, cityTitle, requestLat, requestLng, year)

private fun SavedPrayerLocation.toEntity() = PrayerLocationEntity(
    cityId = cityId,
    cityTitle = cityTitle,
    requestLat = requestLat,
    requestLng = requestLng,
    year = year,
)

private fun PrayerTimeEntity.toDomain() = PrayerDay(date, fajr, sunrise, dhuhr, asr, maghrib, isha)

private fun PrayerDay.toEntity() = PrayerTimeEntity(date, fajr, sunrise, dhuhr, asr, maghrib, isha)
