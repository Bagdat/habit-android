package kz.zhb.database.di

import androidx.room.Room
import kz.zhb.database.HabitDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatabaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), HabitDatabase::class.java, "habit.db").build()
    }

    factory { get<HabitDatabase>().prayerDao() }
}
