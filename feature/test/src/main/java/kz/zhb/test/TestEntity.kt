package kz.zhb.test

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.navigation.Navigator
import kz.zhb.test.counter.CounterScreen
import kz.zhb.test.detail.DetailScreen
import kz.zhb.test.info.InfoScreen
import kz.zhb.test.list.ListScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<NavKey>.testEntity(navigator: Navigator) {
    entry<TestKey> {
        CounterScreen { navigator.navigate(ListKey) }
    }

    entry<ListKey> {
        ListScreen(onItemClick = { item -> navigator.navigate(DetailKey(item)) })
    }

    entry<DetailKey> { key ->
        DetailScreen(
            viewModel = koinViewModel { parametersOf(key.item) },
            onNext = { navigator.navigate(InfoKey) },
        )
    }

    entry<InfoKey> {
        InfoScreen { navigator.pop(ListKey) }
    }
}