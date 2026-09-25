import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Корутины + kotlinx-coroutines-test для юнит-тестов. */
class CoroutinesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            add("implementation", lib("kotlinx-coroutines-core"))
            add("implementation", lib("kotlinx-coroutines-android"))
            add("testImplementation", lib("kotlinx-coroutines-test"))
        }
    }
}
