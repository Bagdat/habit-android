plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.android.compose)
    alias(libs.plugins.habit.lifecycle)
    alias(libs.plugins.habit.coroutines)
    alias(libs.plugins.habit.koin)
    alias(libs.plugins.habit.feature.impl)
}

android {
    namespace = "kz.zhb.tasks.impl"
}

dependencies {
    implementation(project(":core:elm"))
}