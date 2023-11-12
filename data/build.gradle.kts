plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":domain"))

    implementation(libs.google.hilt.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.kotlinSerialization)
    kapt(libs.google.hilt.compiler)

    testImplementation(project(":common:testing-utilities"))

    testImplementation(libs.testing.junit)
    testImplementation(libs.testing.mockk)
    testImplementation(libs.testing.mockWebServer)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
}