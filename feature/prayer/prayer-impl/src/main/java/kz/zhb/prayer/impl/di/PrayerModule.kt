package kz.zhb.prayer.impl.di

import kz.zhb.network.api.ApiCreator
import kz.zhb.prayer.api.usecase.GetSchedulersUseCase
import kz.zhb.prayer.impl.BuildConfig
import kz.zhb.prayer.impl.network.PrayerApi
import kz.zhb.prayer.impl.repository.PrayerRepository
import kz.zhb.prayer.impl.repository.PrayerRepositoryImpl
import kz.zhb.prayer.impl.usecase.GetSchedulersUseCaseImpl
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

    factoryOf(::GetSchedulersUseCaseImpl) { bind<GetSchedulersUseCase>() }
}