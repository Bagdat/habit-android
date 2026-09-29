package kz.zhb.prayer.api.model

import java.time.LocalDate
import java.time.LocalTime

/** Расписание на день для города, по которому последний раз синхронизировались. */
data class PrayerDaySchedule(
    val city: String,
    val day: PrayerDay,
)

data class PrayerDay(
    val date: LocalDate,
    val fajr: LocalTime,
    val sunrise: LocalTime,
    val dhuhr: LocalTime,
    val asr: LocalTime,
    val maghrib: LocalTime,
    val isha: LocalTime,
)
