plugins {
    alias(libs.plugins.habit.feature.impl)
}

android {
    namespace = "kz.zhb.onboarding.impl"
}

dependencies {
    implementation(project(":feature:main:main-api"))
}
