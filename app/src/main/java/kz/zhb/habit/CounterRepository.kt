package kz.zhb.habit

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

interface CounterRepository {
    fun load(): Flow<String>
}

internal class CounterRepositoryImpl: CounterRepository {
    override fun load(): Flow<String> = flow {
        delay(2000L.milliseconds)
        emit("Loaded Successfully")
    }
}