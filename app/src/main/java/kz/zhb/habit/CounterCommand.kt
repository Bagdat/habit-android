package kz.zhb.habit

sealed interface CounterCommand {
    data object Load : CounterCommand
}