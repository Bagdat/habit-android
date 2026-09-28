package kz.zhb.prayer.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kz.zhb.network.api.AsyncResult
import kz.zhb.network.api.RequestProcessor
import kz.zhb.prayer.impl.model.City
import kz.zhb.prayer.impl.network.PrayerApi

internal interface PrayerRepository {
    fun getNearestCities(lat: Double, lng: Double): Flow<AsyncResult<List<City>>>
    fun getSchedulers(year: Int, lat: String, lng: String): Flow<AsyncResult<Unit>>
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
    ): Flow<AsyncResult<Unit>> {
        return requestProcessor.runInFlow { api.getSchedulers(year, lat, lng) }.transform { result ->
            when (result) {
                is AsyncResult.Success -> emit(AsyncResult.Success(Unit))
                is AsyncResult.Failure -> emit(result)
            }
        }
    }
}
