package kz.zhb.test.counter

import kz.zhb.elm.Effect

sealed interface CounterEffect : Effect {
    data class ShowToast(val message: String) : CounterEffect
    data object NavigateToOnboarding : CounterEffect
}