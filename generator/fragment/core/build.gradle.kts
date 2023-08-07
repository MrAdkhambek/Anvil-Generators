plugins {
    id("com.adkhambek.android.library")
    id("com.adkhambek.publish")
}

dependencies {
    compileOnly(projects.generator.fragment.stub)
    compileOnly(libs.dagger.dagger)
}