plugins {
    id("com.adkhambek.android.library")
    id("com.adkhambek.publish")
}

android {
    namespace = "com.adkhambek.kunkka.fragment"
}

dependencies {
    compileOnly(projects.generator.fragment.stub)
    compileOnly(libs.androidx.annotation)
    compileOnly(libs.dagger.dagger)
}
