package kz.zhb.elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kz.zhb.elm.State as ElmState

/** val state by viewModel.collectState() */
@Composable
fun <S : ElmState> ElmViewModel<*, S, *, *>.collectState(): State<S> = state.collectAsStateWithLifecycle()

/** Эффекты собираются только когда экран STARTED; при повороте ничего не теряется. */
@Composable
fun <Ef : Effect> ElmViewModel<*, *, Ef, *>.CollectEffects(onEffect: suspend (Ef) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEffect by rememberUpdatedState(onEffect)
    LaunchedEffect(this, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effects.collect { currentOnEffect(it) }
        }
    }
}
