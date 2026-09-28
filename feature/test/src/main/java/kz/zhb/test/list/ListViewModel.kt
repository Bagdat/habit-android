package kz.zhb.test.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update

class ListViewModel(
    private val repository: ListRepository,
) : ElmViewModel<ListEvents, ListState, ListEffect, ListCommand>(initialState = ListState()) {

    init {
        accept(ListEvents.UI.Init)
    }

    override fun Update<ListState, ListEffect, ListCommand>.reduce(event: ListEvents) {
        when (event) {
            ListEvents.UI.Init -> {
                state { copy(isLoading = true) }
                command(ListCommand.Load)
            }

            is ListEvents.UI.ItemClicked -> effect(ListEffect.OpenDetail(event.item))

            is ListEvents.Internal.Loaded -> state { copy(isLoading = false, items = event.items) }
        }
    }

    override fun execute(command: ListCommand): Flow<ListEvents> = when (command) {
        ListCommand.Load -> repository.load().map { ListEvents.Internal.Loaded(it) }
    }
}
