package kz.zhb.prayer.impl.network

import kz.zhb.prayer.impl.model.CitiesResponse
import kz.zhb.prayer.impl.model.PrayerTimesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PrayerApi {

    /** Ближайшие к точке населённые пункты (~30 км), первый — самый близкий. */
    @GET("cities/")
    suspend fun getNearestCities(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("format") format: String = "json",
    ): Response<CitiesResponse>

    /** Работает только с координатами города из /cities, в точности как там записаны. */
    @GET("prayer-times/{year}/{lat}/{lng}")
    suspend fun getSchedulers(
        @Path("year") year: Int,
        @Path("lat") lat: String,
        @Path("lng") lng: String
    ): Response<PrayerTimesResponse>
}
