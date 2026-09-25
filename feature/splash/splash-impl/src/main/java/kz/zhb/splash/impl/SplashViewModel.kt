package kz.zhb.splash.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update
import kotlin.time.Duration.Companion.seconds

internal class SplashViewModel :
    ElmViewModel<SplashEvent, SplashState, SplashEffect, SplashCommand>(initialState = SplashState) {

    init {
        accept(SplashEvent.Init)
    }

    override fun Update<SplashState, SplashEffect, SplashCommand>.reduce(event: SplashEvent) {
        when (event) {
            SplashEvent.Init -> command(SplashCommand.Wait)
            // Навигация — обычный эффект: reducer не знает про Navigator
            SplashEvent.Finished -> effect(SplashEffect.OpenOnboarding)
        }
    }

    override fun execute(command: SplashCommand): Flow<SplashEvent> = when (command) {
        SplashCommand.Wait -> flow {
            delay(1.seconds)
            emit(SplashEvent.Finished)
        }
    }
}
