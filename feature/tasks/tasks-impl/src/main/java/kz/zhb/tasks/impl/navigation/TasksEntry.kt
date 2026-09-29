package kz.zhb.tasks.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kz.zhb.navigation.Navigator
import kz.zhb.tasks.api.navigation.TasksKey
import kz.zhb.tasks.impl.presentation.main.TasksScreen

fun EntryProviderScope<NavKey>.tasksEntry(navigator: Navigator) {
    entry<TasksKey.Main> {
        TasksScreen()
    }
}
