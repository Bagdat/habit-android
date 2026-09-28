package kz.zhb.prayer.impl.model

import com.google.gson.annotations.SerializedName

/** Ответ api.muftyat.kz/cities/?lat=&lng= — города, отсортированные по расстоянию. */
internal data class CitiesResponse(
    @SerializedName("results") val results: List<CityDto>,
)

internal data class CityDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    // Строки, а не Double: prayer-times принимает координаты только в точности как здесь ("74.640000")
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("distance") val distance: Double?,
)
