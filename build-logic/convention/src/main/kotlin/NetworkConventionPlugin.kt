import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Сеть: Retrofit + OkHttp + kotlinx.serialization, Chucker только в debug. */
class NetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            add("api", lib("retrofit"))
            add("api", lib("retrofit-converter-kotlinx-serialization"))
            add("api", lib("kotlinx-serialization-json"))

            add("api", platform(lib("okhttp-bom")))
            add("api", lib("okhttp"))
            add("implementation", lib("okhttp-tls"))
            add("implementation", lib("okhttp-logging-interceptor"))

            add("debugImplementation", lib("chucker"))
            add("releaseImplementation", lib("chucker-no-op"))
        }
    }
}
