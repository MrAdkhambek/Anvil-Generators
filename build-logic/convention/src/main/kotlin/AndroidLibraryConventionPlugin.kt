import com.adkhambek.app.Config
import com.adkhambek.app.configureKotlinAndroid
import com.adkhambek.app.configureTestAndroid
import com.adkhambek.app.kotlinOptions
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureTestAndroid(this)
                defaultConfig.targetSdk = Config.targetSdkVersion

                kotlinOptions {
                    freeCompilerArgs = (freeCompilerArgs + listOf("-Xexplicit-api=warning")).distinct()
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                // TODO
            }
        }
    }
}
