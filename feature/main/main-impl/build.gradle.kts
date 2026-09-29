plugins {
    alias(libs.plugins.habit.feature.impl)
}

android {
    namespace = "kz.zhb.main.impl"
}

dependencies {
    implementation(project(":feature:tasks:tasks-api"))
    implementation(project(":feature:settings:settings-api"))
}
