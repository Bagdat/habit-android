package kz.zhb.prayer.impl.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.model.PrayerDay
import kz.zhb.prayer.api.model.PrayerDaySchedule
import kz.zhb.prayer.impl.local.PrayerLocalDataSource
import kz.zhb.prayer.impl.local.SavedPrayerLocation
import kz.zhb.prayer.impl.model.City
import kz.zhb.prayer.impl.repository.PrayerRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

private val today = LocalDate.of(2026, 9, 29)
private val day = LocalTime.of(12, 0).let { t -> PrayerDay(today, t, t, t, t, t, t) }

private val almaty = City(1, "Алматы қаласы", "43.238293", "76.945465")
private val kaskelen = City(2, "Қаскелең", "43.202449", "76.622278")

// Точки GPS: в центре Алматы, в ~1 км от неё и в Каскелене (~27 км)
private const val ALMATY_LAT = 43.240330
private const val ALMATY_LNG = 76.944488
private const val NEAR_ALMATY_LAT = 43.249
private const val NEAR_ALMATY_LNG = 76.944488
private const val KASKELEN_LAT = 43.2
private const val KASKELEN_LNG = 76.62

class SyncPrayerScheduleUseCaseImplTest {

    private class FakeRepository(
        private val cities: AsyncResult<List<City>> = AsyncResult.Success(listOf(almaty)),
        private val schedule: AsyncResult<List<PrayerDay>> = AsyncResult.Success(listOf(day)),
    ) : PrayerRepository {
        var citiesRequests = 0
        val scheduleRequests = mutableListOf<Triple<Int, String, String>>()

        override fun getNearestCities(lat: Double, lng: Double): Flow<AsyncResult<List<City>>> {
            citiesRequests++
            return flowOf(cities)
        }

        override fun getSchedulers(year: Int, lat: String, lng: String): Flow<AsyncResult<List<PrayerDay>>> {
            scheduleRequests += Triple(year, lat, lng)
            return flowOf(schedule)
        }
    }

    private class FakeLocal(var location: SavedPrayerLocation? = null) : PrayerLocalDataSource {
        var days: List<PrayerDay>? = null

        override suspend fun savedLocation() = location
        override suspend fun saveLocation(location: SavedPrayerLocation) {
            this.location = location
        }
        override suspend fun replaceSchedule(location: SavedPrayerLocation, days: List<PrayerDay>) {
            this.location = location
            this.days = days
        }
        override fun observeDay(date: LocalDate): Flow<PrayerDaySchedule?> = emptyFlow()
    }

    private fun saved(city: City, lat: Double, lng: Double, year: Int = today.year) =
        SavedPrayerLocation(city.id, city.title, lat, lng, year)

    private fun useCase(repository: PrayerRepository, local: PrayerLocalDataSource) =
        SyncPrayerScheduleUseCaseImpl(repository, local, today = { today })

    @Test
    fun `первый запуск — расписание на год по координатам ближайшего города`() = runTest {
        val repository = FakeRepository(cities = AsyncResult.Success(listOf(almaty, kaskelen)))
        val local = FakeLocal()

        val result = useCase(repository, local)(ALMATY_LAT, ALMATY_LNG).toList()

        assertTrue(result.single() is AsyncResult.Success)
        assertEquals(listOf(Triple(2026, "43.238293", "76.945465")), repository.scheduleRequests)
        assertEquals(listOf(day), local.days)
        assertEquals(saved(almaty, ALMATY_LAT, ALMATY_LNG), local.location)
    }

    @Test
    fun `тот же год и то же место — без сети`() = runTest {
        val repository = FakeRepository()
        val local = FakeLocal(saved(almaty, ALMATY_LAT, ALMATY_LNG))

        val result = useCase(repository, local)(NEAR_ALMATY_LAT, NEAR_ALMATY_LNG).toList()

        assertTrue(result.single() is AsyncResult.Success)
        assertEquals(0, repository.citiesRequests)
        assertTrue(repository.scheduleRequests.isEmpty())
    }

    @Test
    fun `сместились, но город тот же — расписание не качаем, точку запоминаем`() = runTest {
        val repository = FakeRepository(cities = AsyncResult.Success(listOf(almaty)))
        val local = FakeLocal(saved(almaty, KASKELEN_LAT, KASKELEN_LNG))

        useCase(repository, local)(ALMATY_LAT, ALMATY_LNG).toList()

        assertEquals(1, repository.citiesRequests)
        assertTrue(repository.scheduleRequests.isEmpty())
        assertNull(local.days)
        assertEquals(saved(almaty, ALMATY_LAT, ALMATY_LNG), local.location)
    }

    @Test
    fun `сменился город — новое расписание`() = runTest {
        val repository = FakeRepository(cities = AsyncResult.Success(listOf(kaskelen)))
        val local = FakeLocal(saved(almaty, ALMATY_LAT, ALMATY_LNG))

        useCase(repository, local)(KASKELEN_LAT, KASKELEN_LNG).toList()

        assertEquals(listOf(Triple(2026, "43.202449", "76.622278")), repository.scheduleRequests)
        assertEquals(kaskelen.id, local.location?.cityId)
    }

    @Test
    fun `наступил новый год — качаем заново даже на том же месте`() = runTest {
        val repository = FakeRepository()
        val local = FakeLocal(saved(almaty, ALMATY_LAT, ALMATY_LNG, year = 2025))

        useCase(repository, local)(ALMATY_LAT, ALMATY_LNG).toList()

        assertEquals(listOf(Triple(2026, "43.238293", "76.945465")), repository.scheduleRequests)
        assertEquals(2026, local.location?.year)
    }

    @Test
    fun `координаты города передаются строкой без изменений`() = runTest {
        val city = City(3, "Әлжан ана", "43.112222", "74.640000")
        val repository = FakeRepository(cities = AsyncResult.Success(listOf(city)))

        useCase(repository, FakeLocal())(42.874, 74.590).toList()

        assertEquals("74.640000", repository.scheduleRequests.single().third)
    }

    @Test
    fun `рядом нет городов — Failure, БД не трогаем`() = runTest {
        val repository = FakeRepository(cities = AsyncResult.Success(emptyList()))
        val local = FakeLocal()

        val result = useCase(repository, local)(46.0, 70.0).toList()

        assertTrue(result.single() is AsyncResult.Failure)
        assertTrue(repository.scheduleRequests.isEmpty())
        assertNull(local.location)
    }

    @Test
    fun `ошибка загрузки расписания — старое расписание остаётся`() = runTest {
        val old = saved(almaty, ALMATY_LAT, ALMATY_LNG, year = 2025)
        val repository = FakeRepository(schedule = AsyncResult.Failure("timeout"))
        val local = FakeLocal(old)

        val result = useCase(repository, local)(ALMATY_LAT, ALMATY_LNG).toList()

        assertEquals("timeout", (result.single() as AsyncResult.Failure).message)
        assertEquals(old, local.location)
        assertNull(local.days)
    }

    @Test
    fun `ошибка поиска городов пробрасывается дальше`() = runTest {
        val repository = FakeRepository(cities = AsyncResult.Failure("offline"))

        val result = useCase(repository, FakeLocal())(ALMATY_LAT, ALMATY_LNG).toList()

        assertEquals("offline", (result.single() as AsyncResult.Failure).message)
    }
}
