plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.feature.impl)
    alias(libs.plugins.habit.koin)
    alias(libs.plugins.habit.network)
}

android {
    namespace = "kz.zhb.prayer.impl"
}

dependencies {
    implementation(project(":core:network:network-api"))
}