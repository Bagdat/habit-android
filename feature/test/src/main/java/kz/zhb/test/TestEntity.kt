package kz.zhb.test

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.navigation.Navigator
import kz.zhb.test.counter.CounterScreen
import kz.zhb.test.detail.DetailScreen
import kz.zhb.test.info.InfoScreen
import kz.zhb.test.list.ListScreen

fun EntryProviderScope<NavKey>.counterEntity(navigator: Navigator) {
    entry<TestKey> {
        CounterScreen { navigator.navigate(ListKey) }
    }

    entry<ListKey> {
        ListScreen { navigator.navigate(DetailKey) }
    }

    entry<DetailKey> {
        DetailScreen { navigator.navigate(InfoKey) }
    }

    entry<InfoKey> {
        InfoScreen { navigator.pop(ListKey) }
    }
}