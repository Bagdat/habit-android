package kz.zhb.settings.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingsKey : NavKey {
    @Serializable
    data object Main: SettingsKey
}