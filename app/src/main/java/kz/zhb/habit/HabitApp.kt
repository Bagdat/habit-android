package kz.zhb.habit

import android.app.Application
import kz.zhb.test.di.testModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HabitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HabitApp)
            modules(testModule)
        }
    }
}
