import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/** Compose: плагин компилятора + BOM и базовые зависимости. Применять поверх android-модуля. */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.getByType<CommonExtension>().buildFeatures.compose = true

        dependencies {
            val bom = platform(lib("androidx-compose-bom"))
            add("implementation", bom)
            add("androidTestImplementation", bom)
            add("implementation", lib("androidx-compose-ui"))
            add("implementation", lib("androidx-compose-ui-graphics"))
            add("implementation", lib("androidx-compose-ui-tooling-preview"))
            add("implementation", lib("androidx-compose-material3"))
            add("implementation", lib("androidx-activity-compose"))
            add("debugImplementation", lib("androidx-compose-ui-tooling"))

            add("androidTestImplementation", lib("androidx-compose-ui-test-junit4"))
            add("debugImplementation", lib("androidx-compose-ui-test-manifest"))
        }
    }
}
