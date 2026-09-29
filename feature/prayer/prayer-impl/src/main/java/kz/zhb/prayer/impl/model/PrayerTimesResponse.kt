package kz.zhb.prayer.impl.model

import com.google.gson.annotations.SerializedName

internal data class PrayerTimesResponse(
    @SerializedName("result") val result: List<PrayerTimesDto>,
)

/** Время в формате "HH:mm", дата — "yyyy-MM-dd". */
internal data class PrayerTimesDto(
    @SerializedName("Date") val date: String,
    @SerializedName("fajr") val fajr: String,
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("dhuhr") val dhuhr: String,
    @SerializedName("asr") val asr: String,
    @SerializedName("maghrib") val maghrib: String,
    @SerializedName("isha") val isha: String,
)
