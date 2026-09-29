plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.feature.impl)
    alias(libs.plugins.habit.koin)
    alias(libs.plugins.habit.network)
}

android {
    namespace = "kz.zhb.prayer.impl"

    defaultConfig {
        buildConfigField("String", "MUFTYAT_BASE_URL", "\"https://api.muftyat.kz/\"")
    }
}

dependencies {
    implementation(project(":core:network:network-api"))
}