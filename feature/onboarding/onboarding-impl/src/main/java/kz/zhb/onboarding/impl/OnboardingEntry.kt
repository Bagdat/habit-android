package kz.zhb.onboarding.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.main.api.MainKey
import kz.zhb.navigation.Navigator
import kz.zhb.onboarding.api.OnboardingKey

fun EntryProviderScope<NavKey>.onboardingEntry(navigator: Navigator) {
    entry<OnboardingKey> {
        OnboardingScreen(onStart = { navigator.replaceAll(MainKey) })
    }
}
