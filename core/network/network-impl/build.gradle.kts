plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.network)
    alias(libs.plugins.habit.koin)
}

android {
    namespace = "kz.zhb.network.impl"
}

dependencies {
    implementation(project(":core:network:network-api"))
}