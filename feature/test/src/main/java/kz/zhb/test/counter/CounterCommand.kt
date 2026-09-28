package kz.zhb.test.counter

import kz.zhb.elm.Command

sealed interface CounterCommand : Command {
    data object Load : CounterCommand
}