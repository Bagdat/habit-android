package kz.zhb.test.list

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.zhb.test.model.Item
import kotlin.time.Duration.Companion.milliseconds

interface ListRepository {
    fun load(): Flow<List<Item>>
}

internal class ListRepositoryImpl : ListRepository {
    override fun load(): Flow<List<Item>> = flow {
        delay(500.milliseconds)
        emit(
            List(5) { index ->
                Item(id = index + 1L, title = "Item ${index + 1}", description = "Описание элемента ${index + 1}")
            }
        )
    }
}
