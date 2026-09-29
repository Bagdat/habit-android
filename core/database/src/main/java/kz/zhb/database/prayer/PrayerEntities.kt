package kz.zhb.database.prayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

/** Время намаза на один день. В таблице — расписание на год для города из [PrayerLocationEntity]. */
@Entity(tableName = "prayer_times")
data class PrayerTimeEntity(
    @PrimaryKey val date: LocalDate,
    val fajr: LocalTime,
    val sunrise: LocalTime,
    val dhuhr: LocalTime,
    val asr: LocalTime,
    val maghrib: LocalTime,
    val isha: LocalTime,
)

/**
 * Для какого места и года загружено расписание (всегда одна строка).
 * По нему решаем, нужна ли повторная синхронизация.
 */
@Entity(tableName = "prayer_location")
data class PrayerLocationEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val cityId: Long,
    val cityTitle: String,
    /** Точка с GPS, по которой синхронизировали в последний раз. */
    val requestLat: Double,
    val requestLng: Double,
    val year: Int,
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}
