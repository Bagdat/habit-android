package kz.zhb.tasks.impl.presentation.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.zhb.elm.ElmViewModel
import kz.zhb.elm.Update
import kz.zhb.network.api.AsyncResult
import kz.zhb.prayer.api.usecase.GetSchedulersUseCase
import java.time.LocalDate

internal class TasksViewModel(
    private val getSchedulersUseCase: GetSchedulersUseCase,
    private val today: () -> LocalDate = LocalDate::now,
) : ElmViewModel<TasksEvents, TasksState, TasksEffect, TasksCommand>(TasksState()) {

    override fun Update<TasksState, TasksEffect, TasksCommand>.reduce(event: TasksEvents) {
        when (event) {
            is TasksEvents.UI.LoadPrayerSchedulers -> {
                state { copy(isLoading = true, error = null) }
                command(TasksCommand.LoadPrayerSchedulers(event.lat, event.lng))
            }

            TasksEvents.UI.LocationUnavailable -> state {
                copy(isLoading = false, error = "Не удалось определить местоположение. Проверьте, включена ли геолокация")
            }

            is TasksEvents.Internal.PrayerLoaded -> state {
                copy(
                    isLoading = false,
                    city = event.city,
                    prayerDay = event.day,
                    error = if (event.day == null) "Нет расписания на сегодня" else null,
                )
            }

            is TasksEvents.Internal.PrayerFailed -> state { copy(isLoading = false, error = event.message) }
        }
    }

    override fun execute(command: TasksCommand): Flow<TasksEvents> = when (command) {
        is TasksCommand.LoadPrayerSchedulers -> getSchedulersUseCase.invoke(
            command.lat,
            command.lng
        ).map { result ->
            when (result) {
                is AsyncResult.Success -> TasksEvents.Internal.PrayerLoaded(
                    city = result.data.city,
                    day = result.data.dayOf(today())
                )
                is AsyncResult.Failure -> TasksEvents.Internal.PrayerFailed(result.message)
            }
        }
    }

    // Повторный запрос (например, «Повторить») отменяет предыдущий
    override fun keyOf(command: TasksCommand): Any = command::class
}
