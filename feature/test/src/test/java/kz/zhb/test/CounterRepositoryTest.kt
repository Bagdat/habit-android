package kz.zhb.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class CounterRepositoryTest {

    private val repository = CounterRepositoryImpl()

    @Test
    fun `load отдаёт одно сообщение`() = runTest {
        val values = repository.load().toList()

        assertEquals(listOf("Loaded Successfully"), values)
    }

    @Test
    fun `load отдаёт значение через 2 секунды`() = runTest {
        // runTest использует виртуальное время: delay не ждёт по-настоящему
        repository.load().toList()

        assertEquals(2000L, testScheduler.currentTime)
    }

    @Test
    fun `до 2 секунд ничего не приходит`() = runTest {
        val values = mutableListOf<String>()
        val job = launch { repository.load().collect { values += it } }

        advanceTimeBy(1999.milliseconds)
        runCurrent()
        assertTrue(values.isEmpty())

        advanceTimeBy(1.milliseconds)
        runCurrent()
        assertEquals(listOf("Loaded Successfully"), values)

        job.cancel()
    }
}
