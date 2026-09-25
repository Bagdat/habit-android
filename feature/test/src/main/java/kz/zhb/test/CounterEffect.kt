package kz.zhb.test

import kz.zhb.elm.Effect

sealed interface CounterEffect : Effect {
    data class ShowToast(val message: String) : CounterEffect
}