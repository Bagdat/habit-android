package kz.zhb.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.navigation.Navigator
import kz.zhb.settings.api.navigation.SettingsKey
import kz.zhb.settings.impl.presentation.main.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry(navigator: Navigator) {
    entry<SettingsKey.Main> {
        SettingsScreen()
    }
}
