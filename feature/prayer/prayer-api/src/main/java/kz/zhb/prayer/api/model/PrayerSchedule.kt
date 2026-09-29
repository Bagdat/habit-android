package kz.zhb.prayer.api.model

import java.time.LocalDate
import java.time.LocalTime

/** Расписание намаза на год для ближайшего к координатам населённого пункта. */
data class PrayerSchedule(
    val city: String,
    val days: List<PrayerDay>,
) {
    fun dayOf(date: LocalDate): PrayerDay? = days.firstOrNull { it.date == date }
}

data class PrayerDay(
    val date: LocalDate,
    val fajr: LocalTime,
    val sunrise: LocalTime,
    val dhuhr: LocalTime,
    val asr: LocalTime,
    val maghrib: LocalTime,
    val isha: LocalTime,
)
