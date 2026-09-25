import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** DI: Koin для Compose. */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            add("implementation", platform(lib("koin-bom")))
            add("implementation", lib("koin-androidx-compose"))
        }
    }
}
