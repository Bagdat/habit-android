package kz.zhb.test.detail

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

internal interface DetailRepository {
    fun loadStats(id: Long): Flow<String>
}

internal class DetailRepositoryImpl : DetailRepository {
    override fun loadStats(id: Long): Flow<String> = flow {
        delay(700.milliseconds)
        emit("Просмотров: ${id * 42}")
    }
}
