package kz.zhb.prayer.api.usecase

import kotlinx.coroutines.flow.Flow
import kz.zhb.prayer.api.model.PrayerDaySchedule
import java.time.LocalDate

/** Расписание на [date] из БД; эмитит заново после каждой синхронизации. null — ещё не синхронизировали. */
interface ObservePrayerDayUseCase {
    operator fun invoke(date: LocalDate): Flow<PrayerDaySchedule?>
}
