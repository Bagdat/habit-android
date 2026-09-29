package kz.zhb.tasks.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.model.PrayerDay
import kz.zhb.prayer.api.model.PrayerSchedule
import kz.zhb.prayer.api.usecase.GetSchedulersUseCase
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

    private fun day(date: LocalDate) = LocalTime.of(12, 0).let { t -> PrayerDay(date, t, t, t, t, t, t) }

    private class FakeUseCase(private val result: AsyncResult<PrayerSchedule>) : GetSchedulersUseCase {
        val requests = mutableListOf<Pair<Double, Double>>()
        override fun invoke(lat: Double, lng: Double): Flow<AsyncResult<PrayerSchedule>> {
            requests += lat to lng
            return flowOf(result)
        }
    }

    @Before
    fun setUp() = Dispatchers.setMain(dispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `координаты запускают загрузку`() {
        val viewModel = TasksViewModel(FakeUseCase(AsyncResult.Failure("")), today = { today })

        val update = viewModel.reduceForTest(TasksState(), TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))

        assertTrue(update.state.isLoading)
        assertEquals(listOf(TasksCommand.LoadPrayerSchedulers(43.2, 76.9)), update.commands)
    }

    @Test
    fun `из годового расписания в состояние попадает сегодняшний день`() = runTest(dispatcher) {
        val schedule = PrayerSchedule("Алматы", listOf(day(today.minusDays(1)), day(today), day(today.plusDays(1))))
        val useCase = FakeUseCase(AsyncResult.Success(schedule))
        val viewModel = TasksViewModel(useCase, today = { today })

        viewModel.accept(TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf(43.2 to 76.9), useCase.requests)
        assertFalse(state.isLoading)
        assertEquals("Алматы", state.city)
        assertEquals(today, state.prayerDay?.date)
        assertNull(state.error)
    }

    @Test
    fun `ошибка сети показывается и позволяет повторить`() = runTest(dispatcher) {
        val viewModel = TasksViewModel(FakeUseCase(AsyncResult.Failure("timeout")), today = { today })

        viewModel.accept(TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("timeout", state.error)
        assertTrue(state.needsPrayerSchedule)
    }

    @Test
    fun `нет сегодняшнего дня в расписании — ошибка`() = runTest(dispatcher) {
        val schedule = PrayerSchedule("Алматы", listOf(day(today.minusDays(1))))
        val viewModel = TasksViewModel(FakeUseCase(AsyncResult.Success(schedule)), today = { today })

        viewModel.accept(TasksEvents.UI.LoadPrayerSchedulers(43.2, 76.9))
        advanceUntilIdle()

        assertNull(viewModel.state.value.prayerDay)
        assertNotNull(viewModel.state.value.error)
    }

    @Test
    fun `геолокация не определилась — ошибка без запроса`() {
        val viewModel = TasksViewModel(FakeUseCase(AsyncResult.Failure("")), today = { today })

        val update = viewModel.reduceForTest(TasksState(isLoading = true), TasksEvents.UI.LocationUnavailable)

        assertFalse(update.state.isLoading)
        assertNotNull(update.state.error)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `после загрузки повторно не грузим`() {
        assertFalse(TasksState(prayerDay = day(today)).needsPrayerSchedule)
        assertFalse(TasksState(isLoading = true).needsPrayerSchedule)
        assertTrue(TasksState(error = "x").needsPrayerSchedule)
    }
}
