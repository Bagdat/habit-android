import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Lifecycle: ViewModel + runtime, включая Compose-интеграцию (viewModel(), collectAsStateWithLifecycle). */
class LifecycleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            add("implementation", lib("androidx-lifecycle-viewmodel-ktx"))
            add("implementation", lib("androidx-lifecycle-runtime-ktx"))
            add("implementation", lib("androidx-lifecycle-viewmodel-compose"))
            add("implementation", lib("androidx-lifecycle-runtime-compose"))
        }
    }
}
