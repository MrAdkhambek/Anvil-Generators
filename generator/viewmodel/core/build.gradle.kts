plugins {
    id("com.adkhambek.android.library")
    id("com.adkhambek.publish")
}

dependencies {
    compileOnly(projects.generator.viewmodel.stub)
    compileOnly(libs.dagger.dagger)
}