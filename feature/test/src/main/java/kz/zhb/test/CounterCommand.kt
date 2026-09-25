package kz.zhb.test

import kz.zhb.elm.Command

sealed interface CounterCommand : Command {
    data object Load : CounterCommand
}