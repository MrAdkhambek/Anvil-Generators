@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "Anvil-generators"

include(
    ":generator:viewmodel:stub",
    ":generator:viewmodel:core",
    ":generator:viewmodel:gen",
)

include(
    ":generator:retrofit:stub",
    ":generator:retrofit:core",
    ":generator:retrofit:gen",
)

include(
    ":generator:fragment:stub",
    ":generator:fragment:core",
    ":generator:fragment:gen",
)
