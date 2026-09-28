package kz.zhb.test.list

import kz.zhb.elm.Effect
import kz.zhb.test.model.Item

sealed interface ListEffect : Effect {
    data class OpenDetail(val item: Item) : ListEffect
    data class ShowError(val message: String) : ListEffect
}
