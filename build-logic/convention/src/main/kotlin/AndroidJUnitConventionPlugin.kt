import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/** Юнит- и инструментальные тесты. Compose-тесты добавляет habit.android.compose. */
class AndroidJUnitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.getByType<CommonExtension>().defaultConfig.testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        dependencies {
            add("testImplementation", lib("junit"))
            add("androidTestImplementation", lib("androidx-junit"))
            add("androidTestImplementation", lib("androidx-espresso-core"))
        }
    }
}
