import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Navigation 3 для core:navigation: runtime + NavDisplay, ViewModel на запись бэкстека,
 * адаптивные сцены, @Serializable ключи. Через api — чтобы фичи видели NavKey и entry<>.
 */
class NavigationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            add("api", lib("androidx-navigation3-runtime"))
            add("api", lib("androidx-navigation3-ui"))
            add("api", lib("androidx-lifecycle-viewmodel-navigation3"))
            add("api", lib("androidx-compose-material3-adaptive-navigation3"))
            add("api", lib("kotlinx-serialization-core"))
        }
    }
}
