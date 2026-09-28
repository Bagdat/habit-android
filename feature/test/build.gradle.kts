plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.android.compose)
    alias(libs.plugins.habit.lifecycle)
    alias(libs.plugins.habit.coroutines)
    alias(libs.plugins.habit.feature.api)
}

android {
    namespace = "kz.zhb.test"
}

dependencies {
    implementation(project(":core:elm"))
}
