package kz.zhb.elm

import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel = Store + Reducer + Actor.
 *
 * Ev — события (удобно делить на Ui и Internal)
 * S — иммутабельное состояние экрана
 * Ef — одноразовые эффекты (навигация, снекбар)
 * C — команды на побочную работу (сеть, БД)
 */
abstract class ElmViewModel<Ev : Event, S : State, Ef : Effect, C : Command>(initialState: S) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    // Channel, а не SharedFlow: эффект не потеряется, пока UI не подписан (например, при повороте)
    private val _effects = Channel<Ef>(Channel.BUFFERED)
    val effects: Flow<Ef> = _effects.receiveAsFlow()

    private val keyedJobs = mutableMapOf<Any, Job>()

    /** Чистая логика: только state/effect/command, никаких suspend-вызовов. */
    protected abstract fun Update<S, Ef, C>.reduce(event: Ev)

    /** Побочная работа. Результаты возвращаются в reduce() как события. */
    protected abstract fun execute(command: C): Flow<Ev>

    /** Команда с ключом отменяет предыдущую команду с тем же ключом (например, повторная загрузка). */
    protected open fun keyOf(command: C): Any? = null

    /** Хук для логирования переходов. */
    protected open fun onTransition(event: Ev, oldState: S, reducer: Update<S, Ef, C>) = Unit

    @MainThread
    fun accept(event: Ev) {
        val oldState = _state.value
        val reducer = Update<S, Ef, C>(oldState).apply { reduce(event) }
        _state.value = reducer.state
        onTransition(event, oldState, reducer)
        reducer.effects.forEach { _effects.trySend(it) }
        reducer.commands.forEach(::launchCommand)
    }

    private fun launchCommand(command: C) {
        val key = keyOf(command)
        if (key != null) keyedJobs.remove(key)?.cancel()

        // viewModelScope работает на Main.immediate, поэтому accept() вызывается на главном потоке
        val job = viewModelScope.launch {
            execute(command).collect { accept(it) }
        }

        if (key != null) {
            keyedJobs[key] = job
            job.invokeOnCompletion { if (keyedJobs[key] === job) keyedJobs.remove(key) }
        }
    }

    /** Прогнать reducer без запуска команд и без изменения состояния — для unit-тестов. */
    @VisibleForTesting
    fun reduceForTest(state: S, event: Ev): Update<S, Ef, C> =
        Update<S, Ef, C>(state).apply { reduce(event) }
}
