package kz.zhb.prayer.impl.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.usecase.GetSchedulersUseCase
import kz.zhb.prayer.impl.repository.PrayerRepository
import java.time.LocalDate

/**
 * muftyat.kz отдаёт расписание только для координат из своего справочника,
 * поэтому сначала ищем ближайший населённый пункт, потом берём расписание по его координатам.
 */
internal class GetSchedulersUseCaseImpl(private val repository: PrayerRepository) :
    GetSchedulersUseCase {

    override fun invoke(lat: Double, lng: Double): Flow<AsyncResult<Unit>> = flow {
        when (val cities = repository.getNearestCities(lat, lng).first()) {
            is AsyncResult.Failure -> emit(cities)
            is AsyncResult.Success -> {
                val city = cities.data.firstOrNull()
                if (city == null) {
                    emit(AsyncResult.Failure("Рядом нет населённого пункта из справочника"))
                } else {
                    emitAll(repository.getSchedulers(LocalDate.now().year, city.lat, city.lng))
                }
            }
        }
    }
}
