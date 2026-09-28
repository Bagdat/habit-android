package kz.zhb.test.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.usecase.GetSchedulersUseCase
import kz.zhb.test.list.ListEffect.*

class ListViewModel(private val getSchedulersUseCase: GetSchedulersUseCase) :
    ElmViewModel<ListEvents, ListState, ListEffect, ListCommand>(initialState = ListState()) {

    init {
        accept(ListEvents.UI.Init)
    }

    override fun Update<ListState, ListEffect, ListCommand>.reduce(event: ListEvents) {
        when (event) {
            ListEvents.UI.Init -> {
                state { copy(isLoading = true) }
                command(ListCommand.Load)
            }

            is ListEvents.UI.ItemClicked -> effect(OpenDetail(event.item))

            is ListEvents.Internal.Loaded -> state { copy(isLoading = false, items = event.items) }
            is ListEvents.Internal.Failure -> effect(ShowError(event.message))
        }
    }

    override fun execute(command: ListCommand): Flow<ListEvents> = when (command) {
        ListCommand.Load -> getSchedulersUseCase.invoke(43.217705, 76.340629).map { result ->
            when (result) {
                is AsyncResult.Success -> ListEvents.Internal.Loaded(emptyList())
                is AsyncResult.Failure -> ListEvents.Internal.Failure(result.message)
            }
        }
    }
}
