plugins {
    alias(libs.plugins.habit.android.library)
    alias(libs.plugins.habit.android.compose)
    alias(libs.plugins.habit.lifecycle)
    alias(libs.plugins.habit.coroutines)
}

android {
    namespace = "kz.zhb.test"
}

dependencies {
    implementation(project(":core:elm"))
}
