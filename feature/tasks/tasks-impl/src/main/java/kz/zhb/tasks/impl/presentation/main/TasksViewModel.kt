package kz.zhb.tasks.impl.presentation.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.usecase.ObservePrayerDayUseCase
import kz.zhb.prayer.api.usecase.SyncPrayerScheduleUseCase
import java.time.LocalDate

/** Экран читает расписание только из БД; сеть лишь обновляет БД через синхронизацию. */
internal class TasksViewModel(
    private val syncPrayerSchedule: SyncPrayerScheduleUseCase,
    private val observePrayerDay: ObservePrayerDayUseCase,
    private val today: () -> LocalDate = LocalDate::now,
) : ElmViewModel<TasksEvents, TasksState, TasksEffect, TasksCommand>(TasksState()) {

    init {
        accept(TasksEvents.UI.Init)
    }

    override fun Update<TasksState, TasksEffect, TasksCommand>.reduce(event: TasksEvents) {
        when (event) {
            TasksEvents.UI.Init -> command(TasksCommand.ObservePrayerDay(today()))

            is TasksEvents.UI.LoadPrayerSchedulers -> {
                state { copy(isSyncing = true, error = null) }
                command(TasksCommand.SyncPrayerSchedule(event.lat, event.lng))
            }

            TasksEvents.UI.LocationUnavailable -> state {
                copy(error = "Не удалось определить местоположение. Проверьте, включена ли геолокация")
            }

            is TasksEvents.Internal.PrayerDayChanged -> state { copy(city = event.city, prayerDay = event.day) }

            TasksEvents.Internal.SyncFinished -> state { copy(isSyncing = false, isSynced = true, error = null) }

            is TasksEvents.Internal.SyncFailed -> state { copy(isSyncing = false, error = event.message) }
        }
    }

    override fun execute(command: TasksCommand): Flow<TasksEvents> = when (command) {
        is TasksCommand.ObservePrayerDay -> observePrayerDay(command.date).map { schedule ->
            TasksEvents.Internal.PrayerDayChanged(city = schedule?.city, day = schedule?.day)
        }

        is TasksCommand.SyncPrayerSchedule -> syncPrayerSchedule(command.lat, command.lng).map { result ->
            when (result) {
                is AsyncResult.Success -> TasksEvents.Internal.SyncFinished
                is AsyncResult.Failure -> TasksEvents.Internal.SyncFailed(result.message)
            }
        }
    }

    // Повторная команда того же типа (например, «Повторить») отменяет предыдущую
    override fun keyOf(command: TasksCommand): Any = command::class
}
