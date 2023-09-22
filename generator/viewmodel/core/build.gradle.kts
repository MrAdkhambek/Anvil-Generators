plugins {
    id("com.adkhambek.android.library")
    id("com.adkhambek.publish")
}

android {
    namespace = "com.adkhambek.kunkka.vm"
}

dependencies {
    compileOnly(projects.generator.viewmodel.stub)
    compileOnly(libs.dagger.dagger)
}
