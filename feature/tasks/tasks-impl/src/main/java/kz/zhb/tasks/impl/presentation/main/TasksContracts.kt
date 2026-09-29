package kz.zhb.tasks.impl.presentation.main

import kz.zhb.elm.Command
import kz.zhb.elm.Effect
import kz.zhb.elm.Event
import kz.zhb.elm.State
import kz.zhb.prayer.api.model.PrayerDay
import java.time.LocalDate

internal data class TasksState(
    /** Расписание на сегодня из БД; null — ещё ни разу не синхронизировали. */
    val city: String? = null,
    val prayerDay: PrayerDay? = null,
    val isSyncing: Boolean = false,
    /** Синхронизация за этот запуск экрана уже прошла успешно. */
    val isSynced: Boolean = false,
    val error: String? = null,
) : State {
    /** Синхронизируем один раз за жизнь экрана; после ошибки — можно повторить. */
    val needsSync: Boolean get() = !isSyncing && !isSynced
}

internal sealed interface TasksEvents : Event {
    sealed interface UI: TasksEvents {
        data object Init : UI
        data class LoadPrayerSchedulers(val lat: Double, val lng: Double) : UI
        data object LocationUnavailable : UI
    }

    sealed interface Internal: TasksEvents {
        data class PrayerDayChanged(val city: String?, val day: PrayerDay?) : Internal
        data object SyncFinished : Internal
        data class SyncFailed(val message: String) : Internal
    }
}

internal sealed interface TasksEffect: Effect

internal sealed interface TasksCommand: Command {
    /** Подписка на БД: живёт всё время, после синхронизации сама присылает новое расписание. */
    data class ObservePrayerDay(val date: LocalDate) : TasksCommand
    data class SyncPrayerSchedule(val lat: Double, val lng: Double): TasksCommand
}
