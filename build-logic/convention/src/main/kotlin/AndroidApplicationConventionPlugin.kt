import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android-приложение: то же, что библиотека, плюс targetSdk. applicationId и версия — в самом модуле. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")
        pluginManager.apply("org.jetbrains.kotlin.plugin.parcelize")
        pluginManager.apply("habit.android.junit")

        extensions.configure<ApplicationExtension> {
            configureAndroid()
            defaultConfig.targetSdk = TARGET_SDK
        }
    }
}
