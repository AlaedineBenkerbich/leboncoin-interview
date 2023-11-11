plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":datasource:api"))
    implementation(project(":domain"))

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)

    testImplementation(libs.testing.junit)
    testImplementation(libs.testing.mockk)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
}