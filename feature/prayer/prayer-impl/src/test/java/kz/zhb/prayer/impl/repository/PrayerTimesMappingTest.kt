package kz.zhb.prayer.impl.repository

import kz.zhb.prayer.impl.model.PrayerTimesDto
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class PrayerTimesMappingTest {

    @Test
    fun `день из ответа muftyat переводится в дату и время`() {
        val dto = PrayerTimesDto(
            date = "2026-01-01",
            fajr = "05:59",
            sunrise = "07:21",
            dhuhr = "11:59",
            asr = "14:48",
            maghrib = "16:30",
            isha = "17:53",
        )

        val day = dto.toDomain()

        assertEquals(LocalDate.of(2026, 1, 1), day.date)
        assertEquals(LocalTime.of(5, 59), day.fajr)
        assertEquals(LocalTime.of(17, 53), day.isha)
    }
}
