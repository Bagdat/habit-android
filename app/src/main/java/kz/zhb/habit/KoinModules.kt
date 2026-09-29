package kz.zhb.habit

import kz.zhb.database.di.DatabaseModule
import kz.zhb.network.impl.NetworkModule
import kz.zhb.prayer.impl.di.PrayerModule
import kz.zhb.tasks.impl.di.TasksModule
import kz.zhb.test.di.testModule

val KoinModules = listOf(
    DatabaseModule,
    NetworkModule,
    PrayerModule,
    TasksModule,
    testModule
)