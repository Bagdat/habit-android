package kz.zhb.test.list

import kz.zhb.elm.Event
import kz.zhb.test.model.Item

sealed interface ListEvents : Event {

    sealed interface UI : ListEvents {
        data object Init : UI
        data class ItemClicked(val item: Item) : UI
    }

    sealed interface Internal : ListEvents {
        data class Loaded(val items: List<Item>) : Internal
        data class Failure(val message: String) : Internal
    }
}
