package kz.zhb.prayer.impl.di

import kz.zhb.network.api.ApiCreator
import kz.zhb.prayer.api.usecase.ObservePrayerDayUseCase
import kz.zhb.prayer.api.usecase.SyncPrayerScheduleUseCase
import kz.zhb.prayer.impl.BuildConfig
import kz.zhb.prayer.impl.local.PrayerLocalDataSource
import kz.zhb.prayer.impl.local.PrayerLocalDataSourceImpl
import kz.zhb.prayer.impl.network.PrayerApi
import kz.zhb.prayer.impl.repository.PrayerRepository
import kz.zhb.prayer.impl.repository.PrayerRepositoryImpl
import kz.zhb.prayer.impl.usecase.ObservePrayerDayUseCaseImpl
import kz.zhb.prayer.impl.usecase.SyncPrayerScheduleUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val PrayerModule = module {
    factory {
        val apiCreator = get<ApiCreator>()
        apiCreator.create(
            api = PrayerApi::class.java,
            baseUrl = BuildConfig.MUFTYAT_BASE_URL
        )
    }

    factoryOf(::PrayerRepositoryImpl) { bind<PrayerRepository>() }

    factoryOf(::PrayerLocalDataSourceImpl) { bind<PrayerLocalDataSource>() }

    // Не factoryOf: у use case параметр today со значением по умолчанию (для тестов)
    factory<SyncPrayerScheduleUseCase> { SyncPrayerScheduleUseCaseImpl(get(), get()) }

    factoryOf(::ObservePrayerDayUseCaseImpl) { bind<ObservePrayerDayUseCase>() }
}
