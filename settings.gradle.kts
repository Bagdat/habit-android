pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "habit-android"

include(":app")

include(":core:elm")
include(":core:navigation")
include(":core:network")
include(":core:network:network-api")
include(":core:network:network-impl")

include(":feature:test")
include(":feature:splash:splash-api")
include(":feature:splash:splash-impl")
include(":feature:onboarding:onboarding-api")
include(":feature:onboarding:onboarding-impl")
include(":feature:main:main-api")
include(":feature:main:main-impl")
