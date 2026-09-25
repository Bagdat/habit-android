plugins {
    `kotlin-dsl`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.habit.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.habit.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = libs.plugins.habit.android.compose.get().pluginId
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("network") {
            id = libs.plugins.habit.network.get().pluginId
            implementationClass = "NetworkConventionPlugin"
        }
        register("androidJUnit") {
            id = libs.plugins.habit.android.junit.get().pluginId
            implementationClass = "AndroidJUnitConventionPlugin"
        }
        register("coroutines") {
            id = libs.plugins.habit.coroutines.get().pluginId
            implementationClass = "CoroutinesConventionPlugin"
        }
        register("koin") {
            id = libs.plugins.habit.koin.get().pluginId
            implementationClass = "KoinConventionPlugin"
        }
        register("lifecycle") {
            id = libs.plugins.habit.lifecycle.get().pluginId
            implementationClass = "LifecycleConventionPlugin"
        }
        register("navigation") {
            id = libs.plugins.habit.navigation.get().pluginId
            implementationClass = "NavigationConventionPlugin"
        }
        register("featureApi") {
            id = libs.plugins.habit.feature.api.get().pluginId
            implementationClass = "FeatureApiConventionPlugin"
        }
        register("featureImpl") {
            id = libs.plugins.habit.feature.impl.get().pluginId
            implementationClass = "FeatureImplConventionPlugin"
        }
    }
}
