package kz.zhb.prayer.impl.usecase

import kotlinx.coroutines.flow.Flow
import kz.zhb.prayer.api.model.PrayerDaySchedule
import kz.zhb.prayer.api.usecase.ObservePrayerDayUseCase
import kz.zhb.prayer.impl.local.PrayerLocalDataSource
import java.time.LocalDate

internal class ObservePrayerDayUseCaseImpl(private val local: PrayerLocalDataSource) : ObservePrayerDayUseCase {
    override fun invoke(date: LocalDate): Flow<PrayerDaySchedule?> = local.observeDay(date)
}
