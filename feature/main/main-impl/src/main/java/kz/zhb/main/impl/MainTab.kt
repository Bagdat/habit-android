package kz.zhb.main.impl

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import kz.zhb.settings.api.navigation.SettingsKey
import kz.zhb.tasks.api.navigation.TasksKey

internal enum class MainTab(
    val key: NavKey,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    Tasks(TasksKey.Main, R.string.main_tab_tasks, R.drawable.ic_tab_tasks),
    Settings(SettingsKey.Main, R.string.main_tab_settings, R.drawable.ic_tab_settings),
}
