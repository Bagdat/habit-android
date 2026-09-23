package kz.zhb.elm

/**
 * Результат одного шага reducer'а: новое состояние + эффекты + команды.
 * Внутри reduce() пишем: state { copy(...) }, effect(...), command(...).
 */
class Update<S, Ef, C> internal constructor(initial: S) {
    var state: S = initial
        private set

    private val _effects = mutableListOf<Ef>()
    private val _commands = mutableListOf<C>()
    val effects: List<Ef> get() = _effects
    val commands: List<C> get() = _commands

    fun state(block: S.() -> S) { state = state.block() }
    fun effect(effect: Ef) { _effects += effect }
    fun command(command: C) { _commands += command }
}