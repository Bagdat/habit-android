package kz.zhb.habit

import android.app.Application
import kz.zhb.elm.Elm
import kz.zhb.elm.LogcatElmLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HabitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Elm.logger = LogcatElmLogger()
        }

        startKoin {
            androidContext(this@HabitApp)
            modules(KoinModules)
        }
    }
}
