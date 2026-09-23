package kz.zhb.habit

sealed interface CounterEvents {
    sealed interface UI : CounterEvents {
        data object Init : UI
        data object Add : UI
        data object Subtract : UI
    }

    sealed interface Internal : CounterEvents {
        data class Loaded(val message: String) : Internal
    }
}