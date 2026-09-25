import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * feature:*-impl — экраны, ViewModel (ELM), регистрация entry. Зависит только от чужих -api.
 * Свой -api подключается автоматически: :feature:main:main-impl → :feature:main:main-api.
 */
class FeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("habit.android.library")
        pluginManager.apply("habit.android.compose")
        pluginManager.apply("habit.lifecycle")
        pluginManager.apply("habit.coroutines")

        val ownApi = path.removeSuffix("-impl") + "-api"

        dependencies {
            add("api", project(ownApi))
            add("implementation", project(":core:navigation"))
            add("implementation", project(":core:elm"))
        }
    }
}
