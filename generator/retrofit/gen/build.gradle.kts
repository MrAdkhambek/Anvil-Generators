@file:Suppress(
    "DSL_SCOPE_VIOLATION",
    "UnstableApiUsage"
)

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.adkhambek.kotlin")
    alias(libs.plugins.kotlin.ksp)
    id("com.adkhambek.publish")
}

kotlin {
    explicitApi()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=com.squareup.anvil.annotations.ExperimentalAnvilApi"
    }
}

dependencies {
    implementation(projects.generator.retrofit.core)
    implementation(projects.generator.retrofit.stub)

    implementation(libs.dagger.dagger)

    implementation(libs.anvil.compiler.api)
    implementation(libs.anvil.compiler.utils)

    implementation(libs.kotlinpoet.core)
    ksp(libs.kotlinpoet.ksp)

    implementation(libs.auto.service.annotations)
    ksp(libs.auto.service.ksp)

    testImplementation(testFixtures(libs.anvil.compiler.utils))
    testImplementation(libs.junit4)
}
