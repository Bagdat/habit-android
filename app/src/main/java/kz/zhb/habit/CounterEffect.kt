package kz.zhb.habit

sealed interface CounterEffect {
    data class ShowToast(val message: String) : CounterEffect
}