package kz.zhb.test.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val title: String,
    val description: String,
)
