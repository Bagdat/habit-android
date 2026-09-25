package kz.zhb.test

import kz.zhb.elm.State

data class CounterState(
    val isLoading: Boolean = false,
    val count: Int = 0
) : State
