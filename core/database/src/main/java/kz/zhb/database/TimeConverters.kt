package kz.zhb.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

/** LocalDate — день от эпохи (сортируется и сравнивается в SQL), LocalTime — секунда дня. */
internal class TimeConverters {

    @TypeConverter
    fun fromDate(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun toDate(epochDay: Long?): LocalDate? = epochDay?.let(LocalDate::ofEpochDay)

    @TypeConverter
    fun fromTime(time: LocalTime?): Int? = time?.toSecondOfDay()

    @TypeConverter
    fun toTime(secondOfDay: Int?): LocalTime? = secondOfDay?.let { LocalTime.ofSecondOfDay(it.toLong()) }
}
