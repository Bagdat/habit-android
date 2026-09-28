package kz.zhb.test.list

import kz.zhb.elm.Command

sealed interface ListCommand : Command {
    data object Load : ListCommand
}
