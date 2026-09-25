package kz.zhb.main.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.main.api.MainKey
import kz.zhb.navigation.Navigator

@Suppress("UNUSED_PARAMETER") // navigator понадобится, когда из Main появятся переходы
fun EntryProviderScope<NavKey>.mainEntry(navigator: Navigator) {
    entry<MainKey> {
        MainScreen()
    }
}
