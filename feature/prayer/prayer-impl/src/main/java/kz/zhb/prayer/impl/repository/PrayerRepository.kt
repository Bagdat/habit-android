package kz.zhb.prayer.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kz.zhb.network.api.AsyncResult
import kz.zhb.network.api.RequestProcessor
import kz.zhb.prayer.api.model.PrayerDay
import kz.zhb.prayer.impl.model.City
import kz.zhb.prayer.impl.model.PrayerTimesDto
import kz.zhb.prayer.impl.network.PrayerApi
import java.time.LocalDate
import java.time.LocalTime

internal interface PrayerRepository {
    fun getNearestCities(lat: Double, lng: Double): Flow<AsyncResult<List<City>>>
    fun getSchedulers(year: Int, lat: String, lng: String): Flow<AsyncResult<List<PrayerDay>>>
}

internal class PrayerRepositoryImpl(
    private val api: PrayerApi,
    private val requestProcessor: RequestProcessor
) : PrayerRepository {

    override fun getNearestCities(lat: Double, lng: Double): Flow<AsyncResult<List<City>>> {
        return requestProcessor.runInFlow { api.getNearestCities(lat, lng) }.transform { result ->
            when (result) {
                is AsyncResult.Success -> emit(
                    AsyncResult.Success(result.data.results.map { City(it.id, it.title, it.lat, it.lng) })
                )
                is AsyncResult.Failure -> emit(result)
            }
        }
    }

    override fun getSchedulers(
        year: Int,
        lat: String,
        lng: String
    ): Flow<AsyncResult<List<PrayerDay>>> {
        return requestProcessor.runInFlow { api.getSchedulers(year, lat, lng) }.transform { result ->
            when (result) {
                is AsyncResult.Success -> emit(AsyncResult.Success(result.data.result.map { it.toDomain() }))
                is AsyncResult.Failure -> emit(result)
            }
        }
    }
}

internal fun PrayerTimesDto.toDomain() = PrayerDay(
    date = LocalDate.parse(date),
    fajr = LocalTime.parse(fajr),
    sunrise = LocalTime.parse(sunrise),
    dhuhr = LocalTime.parse(dhuhr),
    asr = LocalTime.parse(asr),
    maghrib = LocalTime.parse(maghrib),
    isha = LocalTime.parse(isha),
)
