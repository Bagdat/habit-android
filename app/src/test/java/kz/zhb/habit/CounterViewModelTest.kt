package kz.zhb.habit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class CounterViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private class FakeRepository(private val message: String = "ok") : CounterRepository {
        override fun load(): Flow<String> = flow {
            delay(1000.milliseconds)
            emit(message)
        }
    }

    @Before
    fun setUp() {
        // viewModelScope работает на Dispatchers.Main — подменяем на тестовый
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Reducer: чистая логика, без корутин ---

    @Test
    fun `Init включает загрузку и отправляет Load`() {
        val update = CounterViewModel(FakeRepository())
            .reduceForTest(CounterState(), CounterEvents.UI.Init)

        assertTrue(update.state.isLoading)
        assertEquals(listOf(CounterCommand.Load), update.commands)
        assertTrue(update.effects.isEmpty())
    }

    @Test
    fun `Add увеличивает счётчик`() {
        val update = CounterViewModel(FakeRepository())
            .reduceForTest(CounterState(count = 5), CounterEvents.UI.Add)

        assertEquals(6, update.state.count)
        assertTrue(update.commands.isEmpty())
    }

    @Test
    fun `Subtract уменьшает счётчик`() {
        val update = CounterViewModel(FakeRepository())
            .reduceForTest(CounterState(count = 5), CounterEvents.UI.Subtract)

        assertEquals(4, update.state.count)
    }

    @Test
    fun `Loaded выключает загрузку и показывает тост`() {
        val update = CounterViewModel(FakeRepository())
            .reduceForTest(CounterState(isLoading = true), CounterEvents.Internal.Loaded("hi"))

        assertEquals(false, update.state.isLoading)
        assertEquals(listOf(CounterEffect.ShowToast("hi")), update.effects)
    }

    // --- Интеграция: reducer + execute + repository ---

    @Test
    fun `при создании грузит данные и показывает тост`() = runTest(dispatcher) {
        val viewModel = CounterViewModel(FakeRepository("done"))

        advanceTimeBy(500)
        assertTrue(viewModel.state.value.isLoading)

        advanceUntilIdle()
        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(CounterEffect.ShowToast("done"), viewModel.effects.first())
    }

    @Test
    fun `счётчик работает во время загрузки`() = runTest(dispatcher) {
        val viewModel = CounterViewModel(FakeRepository())

        viewModel.accept(CounterEvents.UI.Add)
        viewModel.accept(CounterEvents.UI.Add)
        viewModel.accept(CounterEvents.UI.Subtract)

        assertEquals(1, viewModel.state.value.count)
        assertTrue(viewModel.state.value.isLoading)
    }
}
