plugins {
    id("com.adkhambek.kotlin")
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.androidx.annotation)
//    api(libs.javax.inject)
}
