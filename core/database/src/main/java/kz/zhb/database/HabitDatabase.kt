package kz.zhb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.zhb.database.prayer.PrayerDao
import kz.zhb.database.prayer.PrayerLocationEntity
import kz.zhb.database.prayer.PrayerTimeEntity

/**
 * Единая БД приложения. Таблицы всех фич живут здесь, фичи получают только свой DAO через Koin.
 *
 * Новая таблица: Entity + Dao в своём пакете, добавить в entities, поднять version
 * и описать миграцию (схемы лежат в core/database/schemas).
 */
@Database(
    entities = [
        PrayerTimeEntity::class,
        PrayerLocationEntity::class,
    ],
    version = 1,
)
@TypeConverters(TimeConverters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
}
