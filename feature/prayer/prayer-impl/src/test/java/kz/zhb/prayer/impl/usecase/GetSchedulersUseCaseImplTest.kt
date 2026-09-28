package kz.zhb.prayer.impl.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.impl.model.City
import kz.zhb.prayer.impl.repository.PrayerRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetSchedulersUseCaseImplTest {

    private class FakeRepository(
        private val cities: AsyncResult<List<City>>,
    ) : PrayerRepository {
        val schedulerRequests = mutableListOf<Pair<String, String>>()

        override fun getNearestCities(lat: Double, lng: Double): Flow<AsyncResult<List<City>>> = flowOf(cities)

        override fun getSchedulers(year: Int, lat: String, lng: String): Flow<AsyncResult<Unit>> {
            schedulerRequests += lat to lng
            return flowOf(AsyncResult.Success(Unit))
        }
    }

    private val almaty = City(1, "Алматы қаласы", "43.238293", "76.945465")
    private val kaskelen = City(2, "Қаскелең", "43.202449", "76.622278")

    @Test
    fun `расписание запрашивается по координатам ближайшего города, а не по GPS`() = runTest {
        val repository = FakeRepository(AsyncResult.Success(listOf(almaty, kaskelen)))

        val result = GetSchedulersUseCaseImpl(repository)(43.240330, 76.944488).toList()

        assertEquals(listOf("43.238293" to "76.945465"), repository.schedulerRequests)
        assertTrue(result.single() is AsyncResult.Success)
    }

    @Test
    fun `координаты города передаются строкой без изменений`() = runTest {
        val city = City(3, "Әлжан ана", "43.112222", "74.640000")
        val repository = FakeRepository(AsyncResult.Success(listOf(city)))

        GetSchedulersUseCaseImpl(repository)(42.874, 74.590).toList()

        assertEquals(listOf("43.112222" to "74.640000"), repository.schedulerRequests)
    }

    @Test
    fun `если рядом нет городов — Failure и расписание не запрашивается`() = runTest {
        val repository = FakeRepository(AsyncResult.Success(emptyList()))

        val result = GetSchedulersUseCaseImpl(repository)(46.0, 70.0).toList()

        assertTrue(result.single() is AsyncResult.Failure)
        assertTrue(repository.schedulerRequests.isEmpty())
    }

    @Test
    fun `ошибка поиска городов пробрасывается дальше`() = runTest {
        val repository = FakeRepository(AsyncResult.Failure("timeout"))

        val result = GetSchedulersUseCaseImpl(repository)(43.24, 76.94).toList()

        assertEquals("timeout", (result.single() as AsyncResult.Failure).message)
        assertTrue(repository.schedulerRequests.isEmpty())
    }
}
