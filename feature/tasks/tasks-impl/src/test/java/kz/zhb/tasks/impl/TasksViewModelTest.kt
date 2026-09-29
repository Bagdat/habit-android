package kz.zhb.tasks.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.model.PrayerDay
import kz.zhb.prayer.api.model.PrayerDaySchedule
import kz.zhb.prayer.api.usecase.ObservePrayerDayUseCase
import kz.zhb.prayer.api.usecase.SyncPrayerScheduleUseCase
import kz.zhb.tasks.impl.presentation.main.TasksCommand
import kz.zhb.tasks.impl.presentation.main.TasksEvents
import kz.zhb.tasks.impl.presentation.main.TasksState
import kz.zhb.tasks.impl.presentation.main.TasksViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val today = LocalDate.of(2026, 9, 29)
    private val day = LocalTime.of(12, 0).let { t -> PrayerDay(today, t, t, t, t, t, t) }

    /** Имитация БД: sync пишет в неё, observe отдаёт текущее значение. */
    private class FakeDb(initial: PrayerDaySchedule? = null) : ObservePrayerDayUseCase {
        val schedule = MutableStateFlow(initial)
        val observedDates = mutableListOf<LocalDate>()

        override fun invoke(date: LocalDate): Flow<PrayerDaySchedule?> {
            observedDates += date
            return schedule
        }
    }

    private class FakeSync(
        private val db: FakeDb,
        private val result: AsyncResult<Unit>,
        private val writes: PrayerDaySchedule? = null,
    ) : SyncPrayerScheduleUseCase {
        val requests = mutableListOf<Pair<Double, Double>>()

        override fun invoke(lat: Double, lng: Double): Flow<AsyncResult<Unit>> {
            requests += lat to lng
            if (writes != null) db.schedule.value = writes
            return flowOf(result)
        }
    }

    private fun viewModel(db: FakeDb, sync: SyncPrayerScheduleUseCase) =
        TasksViewModel(sync, db, today = { today })

    @Before
    fun setUp() = Dispatchers.setMain(dispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `при открытии расписание берётся из БД без сети`() = runTest(dispatcher) {
        val db = FakeDb(PrayerDaySchedule("Алматы", day))
        val sync = FakeSync(db, AsyncResult.Success(Unit))

        val viewModel = viewModel(db, sync)
        advanceUntilIdle()

        assertEquals(listOf(today), db.observedDates)
        assertEquals("Алматы", viewModel.state.value.city)
        assertEquals(day, viewModel.state.value.prayerDay)
        assertTrue(sync.requests.isEmpty())
    }

    @Test
    fun `координаты запускают синхронизацию`() {
        val db = FakeDb()
        val viewModel = viewModel(db, FakeSync(db, AsyncResult.Success(Unit)))

        val update = viewModel.reduceForTest(TasksState(), TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))

        assertTrue(update.state.isSyncing)
        assertEquals(listOf(TasksCommand.SyncPrayerSchedule(43.2, 76.9)), update.commands)
    }

    @Test
    fun `после синхронизации новое расписание приходит из БД`() = runTest(dispatcher) {
        val db = FakeDb()
        val sync = FakeSync(db, AsyncResult.Success(Unit), writes = PrayerDaySchedule("Алматы", day))
        val viewModel = viewModel(db, sync)

        viewModel.accept(TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf(43.2 to 76.9), sync.requests)
        assertEquals(day, state.prayerDay)
        assertFalse(state.isSyncing)
        assertTrue(state.isSynced)
        assertFalse(state.needsSync)
    }

    @Test
    fun `ошибка синхронизации — старое расписание остаётся, можно повторить`() = runTest(dispatcher) {
        val db = FakeDb(PrayerDaySchedule("Алматы", day))
        val viewModel = viewModel(db, FakeSync(db, AsyncResult.Failure("timeout")))

        viewModel.accept(TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("timeout", state.error)
        assertEquals(day, state.prayerDay)
        assertTrue(state.needsSync)
    }

    @Test
    fun `геолокация не определилась — ошибка без запроса`() {
        val db = FakeDb()
        val viewModel = viewModel(db, FakeSync(db, AsyncResult.Success(Unit)))

        val update = viewModel.reduceForTest(TasksState(), TasksEvents.UI.LocationUnavailable)

        assertNotNull(update.state.error)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `в БД ещё пусто — расписания нет`() = runTest(dispatcher) {
        val db = FakeDb()
        val viewModel = viewModel(db, FakeSync(db, AsyncResult.Success(Unit)))
        advanceUntilIdle()

        assertNull(viewModel.state.value.prayerDay)
        assertTrue(viewModel.state.value.needsSync)
    }
}
