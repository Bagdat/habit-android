plugins {
    alias(libs.plugins.habit.android.application)
    alias(libs.plugins.habit.android.compose)
    alias(libs.plugins.habit.koin)
}

android {
    namespace = "kz.zhb.habit"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(project(":core:navigation"))

    implementation(project(":feature:test"))
}