package kz.zhb.test.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update
import kz.zhb.test.model.Item

internal class DetailViewModel(
    item: Item,
    private val repository: DetailRepository,
) : ElmViewModel<DetailEvents, DetailState, DetailEffect, DetailCommand>(
    initialState = DetailState(item = item),
) {

    init {
        accept(DetailEvents.UI.Init)
    }

    override fun Update<DetailState, DetailEffect, DetailCommand>.reduce(event: DetailEvents) {
        when (event) {
            DetailEvents.UI.Init -> command(DetailCommand.LoadStats(state.item.id))
            DetailEvents.UI.NextClicked -> effect(DetailEffect.OpenInfo)
            is DetailEvents.Internal.StatsLoaded -> state { copy(stats = event.stats) }
        }
    }

    override fun execute(command: DetailCommand): Flow<DetailEvents> = when (command) {
        is DetailCommand.LoadStats -> repository.loadStats(command.id).map { DetailEvents.Internal.StatsLoaded(it) }
    }
}
