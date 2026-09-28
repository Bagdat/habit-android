package kz.zhb.test.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update

class ListViewModel: ElmViewModel<ListEvents, ListState, ListEffect, ListCommand>(ListState(isLoading = true)) {

    init {
        println("List Screen")
    }

    override fun Update<ListState, ListEffect, ListCommand>.reduce(event: ListEvents) {}

    override fun execute(command: ListCommand): Flow<ListEvents> = emptyFlow()
}