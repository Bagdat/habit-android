package kz.zhb.test.list

import kz.zhb.elm.State
import kz.zhb.test.model.Item

data class ListState(
    val isLoading: Boolean = true,
    val items: List<Item> = emptyList(),
) : State
