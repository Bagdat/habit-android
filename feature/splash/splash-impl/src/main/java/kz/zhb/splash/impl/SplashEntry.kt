package kz.zhb.splash.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.navigation.Navigator
import kz.zhb.onboarding.api.OnboardingKey
import kz.zhb.splash.api.SplashKey

fun EntryProviderScope<NavKey>.splashEntry(navigator: Navigator) {
    entry<SplashKey> {
        SplashScreen(onFinished = { navigator.replaceAll(OnboardingKey) })
    }
}
