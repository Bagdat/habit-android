package kz.zhb.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

/** Экраны фичи регистрируются через extension: fun EntryProviderScope<NavKey>.splashEntry(navigator: Navigator) */
typealias Entries = EntryProviderScope<NavKey>.(navigator: Navigator) -> Unit

/**
 * Корень навигации. Бэкстек переживает поворот и смерть процесса (ключи — @Serializable),
 * у каждого экрана свой ViewModelStore: ViewModel очищается, когда экран уходит из стека.
 */
@Composable
fun NavigationHost(
    start: NavKey,
    modifier: Modifier = Modifier,
    entries: Entries,
) {
    val backStack = rememberNavBackStack(start)
    val navigator = remember(backStack) { BackStackNavigator(backStack) }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = navigator::back,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider { entries(navigator) },
    )
}
