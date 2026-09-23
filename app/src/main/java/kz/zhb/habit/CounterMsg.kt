package kz.zhb.habit

sealed interface CounterMsg {
    data object Increment : CounterMsg
    data object Decrement : CounterMsg
}