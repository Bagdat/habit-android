package kz.zhb.test

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kz.zhb.test.model.Item

@Serializable
data object TestKey : NavKey

@Serializable
data object ListKey : NavKey

/** Объект передаётся прямо в ключе: Item тоже должен быть @Serializable. */
@Serializable
data class DetailKey(val item: Item) : NavKey

@Serializable
data object InfoKey : NavKey

