package kz.zhb.habit

import kz.zhb.network.impl.NetworkModule
import kz.zhb.prayer.impl.di.PrayerModule
import kz.zhb.test.di.testModule

val KoinModules = listOf(
    NetworkModule,
    PrayerModule,
    testModule
)