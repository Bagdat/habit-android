package kz.zhb.tasks.impl.di

import kz.zhb.tasks.impl.presentation.main.TasksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val TasksModule = module {
    viewModel { TasksViewModel(syncPrayerSchedule = get(), observePrayerDay = get()) }
}
