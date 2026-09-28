plugins {
    alias(libs.plugins.habit.feature.api)
}

android {
    namespace = "kz.zhb.prayer.api"
}

dependencies {
    api(project(":core:network:network-api"))
}