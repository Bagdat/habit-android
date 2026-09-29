plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.room)
    alias(libs.plugins.habit.koin)
    alias(libs.plugins.habit.coroutines)
}

android {
    namespace = "kz.zhb.database"
}
