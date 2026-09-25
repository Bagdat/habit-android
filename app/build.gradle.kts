plugins {
    alias(libs.plugins.habit.android.application)
    alias(libs.plugins.habit.android.compose)
}

android {
    namespace = "kz.zhb.habit"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(project(":core:navigation"))

    implementation(project(":feature:splash:splash-impl"))
    implementation(project(":feature:onboarding:onboarding-impl"))
    implementation(project(":feature:main:main-impl"))
}