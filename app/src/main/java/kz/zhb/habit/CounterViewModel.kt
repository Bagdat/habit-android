package kz.zhb.habit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update

class CounterViewModel(
    private val repository: CounterRepository = CounterRepositoryImpl(),
) : ElmViewModel<CounterEvents, CounterState, CounterEffect, CounterCommand>(initialState = CounterState()) {

    init {
        accept(CounterEvents.UI.Init)
    }

    override fun Update<CounterState, CounterEffect, CounterCommand>.reduce(event: CounterEvents) {
        when (event) {
            CounterEvents.UI.Init -> {
                state { copy(isLoading = true) }
                command(CounterCommand.Load)
            }

            CounterEvents.UI.Add -> {
                state { copy(count = count + 1) }
            }

            CounterEvents.UI.Subtract -> {
                state { copy(count = count - 1) }
            }

            is CounterEvents.Internal.Loaded -> {
                state { copy(isLoading = false) }
                effect(CounterEffect.ShowToast(event.message))
            }
        }
    }

    override fun execute(command: CounterCommand): Flow<CounterEvents> = when (command) {
        CounterCommand.Load -> repository.load().map {
            CounterEvents.Internal.Loaded(it)
        }
    }
}