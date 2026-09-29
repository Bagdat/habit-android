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

    implementation(project(":core:elm"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network:network-impl"))

    implementation(project(":feature:test"))
    implementation(project(":feature:splash:splash-impl"))
    implementation(project(":feature:onboarding:onboarding-impl"))
    implementation(project(":feature:main:main-impl"))
    implementation(project(":feature:prayer:prayer-impl"))
    implementation(project(":feature:tasks:tasks-impl"))
    implementation(project(":feature:settings:settings-impl"))
}