package kz.zhb.tasks.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface TasksKey : NavKey {
    @Serializable
    data object Main : TasksKey
}
