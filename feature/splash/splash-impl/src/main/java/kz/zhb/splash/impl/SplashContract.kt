package kz.zhb.splash.impl

import kz.zhb.elm.Command
import kz.zhb.elm.Effect
import kz.zhb.elm.Event
import kz.zhb.elm.State

sealed interface SplashEvent : Event {
    data object Init : SplashEvent
    data object Finished : SplashEvent
}

data object SplashState : State

sealed interface SplashEffect : Effect {
    data object OpenOnboarding : SplashEffect
}

sealed interface SplashCommand : Command {
    data object Wait : SplashCommand
}
