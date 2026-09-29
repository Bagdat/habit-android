import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/** Room через KSP. Схемы БД экспортируются в <module>/schemas — их коммитим, по ним пишутся миграции. */
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("androidx.room")

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        dependencies {
            add("api", lib("androidx-room-runtime"))
            add("api", lib("androidx-room-ktx"))
            add("ksp", lib("androidx-room-compiler"))
        }
    }
}
