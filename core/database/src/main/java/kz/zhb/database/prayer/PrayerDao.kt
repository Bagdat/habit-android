package kz.zhb.database.prayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
abstract class PrayerDao {

    @Query("SELECT * FROM prayer_times WHERE date = :date")
    abstract fun observeDay(date: LocalDate): Flow<PrayerTimeEntity?>

    @Query("SELECT * FROM prayer_location WHERE id = ${PrayerLocationEntity.SINGLE_ROW_ID}")
    abstract fun observeLocation(): Flow<PrayerLocationEntity?>

    @Query("SELECT * FROM prayer_location WHERE id = ${PrayerLocationEntity.SINGLE_ROW_ID}")
    abstract suspend fun location(): PrayerLocationEntity?

    @Upsert
    abstract suspend fun upsertLocation(location: PrayerLocationEntity)

    /** Новое расписание целиком заменяет старое — подписчики observeDay не увидят пустую таблицу. */
    @Transaction
    open suspend fun replaceSchedule(location: PrayerLocationEntity, times: List<PrayerTimeEntity>) {
        clearTimes()
        insertTimes(times)
        upsertLocation(location)
    }

    @Query("DELETE FROM prayer_times")
    protected abstract suspend fun clearTimes()

    @Insert
    protected abstract suspend fun insertTimes(times: List<PrayerTimeEntity>)
}
