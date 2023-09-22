plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kotlinLibrary") {
            id = "com.adkhambek.kotlin"
            implementationClass = "KotlinLibraryConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.adkhambek.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}
