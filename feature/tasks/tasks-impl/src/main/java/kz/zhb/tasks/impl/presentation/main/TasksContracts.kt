package kz.zhb.tasks.impl.presentation.main

import kz.zhb.elm.Command
import kz.zhb.elm.Effect
import kz.zhb.elm.Event
import kz.zhb.elm.State
import kz.zhb.prayer.api.model.PrayerDay

internal data class TasksState(
    val isLoading: Boolean = false,
    val city: String? = null,
    val prayerDay: PrayerDay? = null,
    val error: String? = null,
) : State {
    /** Можно запускать загрузку: ещё ничего нет и ничего не грузится. */
    val needsPrayerSchedule: Boolean get() = prayerDay == null && !isLoading
}

internal sealed interface TasksEvents : Event {
    sealed interface UI: TasksEvents {
        data class LoadPrayerSchedulers(val lat: Double, val lng: Double) : UI
        data object LocationUnavailable : UI
    }

    sealed interface Internal: TasksEvents {
        data class PrayerLoaded(val city: String, val day: PrayerDay?) : Internal
        data class PrayerFailed(val message: String) : Internal
    }
}

internal sealed interface TasksEffect: Effect

internal sealed interface TasksCommand: Command {
    data class LoadPrayerSchedulers(val lat: Double, val lng: Double): TasksCommand
}
