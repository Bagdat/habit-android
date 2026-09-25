plugins {
    alias(libs.plugins.habit.feature.impl)
}

android {
    namespace = "kz.zhb.splash.impl"
}

dependencies {
    implementation(project(":feature:onboarding:onboarding-api"))
}
