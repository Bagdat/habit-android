package kz.zhb.main.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.main.api.MainKey
import kz.zhb.navigation.Entries
import kz.zhb.navigation.Navigator

/** [tabs] — entry экранов табов (ключи из [MainTab]), например: { tasksEntry(it); settingsEntry(it) } */
fun EntryProviderScope<NavKey>.mainEntry(navigator: Navigator, tabs: Entries) {
    entry<MainKey> {
        MainScreen(navigator, tabs)
    }
}
