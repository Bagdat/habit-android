package kz.zhb.test.detail

import kz.zhb.elm.Command
import kz.zhb.elm.Effect
import kz.zhb.elm.Event
import kz.zhb.elm.State
import kz.zhb.test.model.Item

internal sealed interface DetailEvents : Event {
    sealed interface UI : DetailEvents {
        data object Init : UI
        data object NextClicked : UI
    }

    sealed interface Internal : DetailEvents {
        data class StatsLoaded(val stats: String) : Internal
    }
}

internal data class DetailState(
    val item: Item,
    val stats: String? = null,
) : State

internal sealed interface DetailEffect : Effect {
    data object OpenInfo : DetailEffect
}

internal sealed interface DetailCommand : Command {
    data class LoadStats(val id: Long) : DetailCommand
}
