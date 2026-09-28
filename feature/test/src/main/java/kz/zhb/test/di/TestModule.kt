package kz.zhb.test.di

import kz.zhb.test.counter.CounterRepository
import kz.zhb.test.counter.CounterRepositoryImpl
import kz.zhb.test.counter.CounterViewModel
import kz.zhb.test.detail.DetailRepository
import kz.zhb.test.detail.DetailRepositoryImpl
import kz.zhb.test.detail.DetailViewModel
import kz.zhb.test.list.ListRepository
import kz.zhb.test.list.ListRepositoryImpl
import kz.zhb.test.list.ListViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val testModule = module {
    factoryOf(::CounterRepositoryImpl) { bind<CounterRepository>() }
    viewModelOf(::CounterViewModel)

    factoryOf(::ListRepositoryImpl) { bind<ListRepository>() }
    viewModelOf(::ListViewModel)

    factoryOf(::DetailRepositoryImpl) { bind<DetailRepository>() }
    viewModelOf(::DetailViewModel)
}
