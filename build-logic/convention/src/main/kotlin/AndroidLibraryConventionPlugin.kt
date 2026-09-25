import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android-библиотека: общая настройка SDK/Java/packaging. Тесты подключаются через habit.android.junit. */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.parcelize")
        pluginManager.apply("habit.android.junit")

        extensions.configure<LibraryExtension> {
            configureAndroid()
        }
    }
}
