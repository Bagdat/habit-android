package kz.zhb.main.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import kz.zhb.navigation.Entries
import kz.zhb.navigation.Navigator

/**
 * Контейнер табов. Экраны табов приходят через [tabs] (регистрируются в app), поэтому main-impl
 * знает только ключи из чужих -api. Состояние каждого таба сохраняется при переключении,
 * ViewModel табов живут в ViewModelStore экрана Main.
 *
 * Системный «назад» возвращает к предыдущему табу, стартовый таб ([MainTab.Tasks]) всегда
 * внизу истории: Tasks → Settings → back → Tasks → back → выход с Main.
 */
@Composable
internal fun MainScreen(navigator: Navigator, tabs: Entries) {
    var history by rememberSaveable(stateSaver = TabHistorySaver) { mutableStateOf(listOf(START_TAB)) }
    val selected = history.last()
    val provider = remember(navigator, tabs) { entryProvider<NavKey> { tabs(navigator) } }
    val stateHolder = rememberSaveableStateHolder()

    BackHandler(enabled = history.size > 1) {
        history = history.dropLast(1)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = tab == selected,
                        onClick = {
                            history = if (tab == START_TAB) listOf(tab) else history - tab + tab
                        },
                        icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                        label = { Text(stringResource(tab.label)) },
                    )
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            stateHolder.SaveableStateProvider(selected.name) {
                provider(selected.key).Content()
            }
        }
    }
}

private val START_TAB = MainTab.Tasks

private val TabHistorySaver = listSaver(
    save = { history -> history.map { it.name } },
    restore = { names -> names.map(MainTab::valueOf) },
)
