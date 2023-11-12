plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

android {
    namespace = "fr.leboncoin.albumreader.data"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":domain"))

    implementation(libs.google.hilt.android)
    implementation(libs.google.hilt.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.kotlinSerialization)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    kapt(libs.google.hilt.compiler)
    ksp(libs.room.compiler)

    testImplementation(project(":common:testing-utilities"))

    testImplementation(libs.testing.junit)
    testImplementation(libs.testing.mockk)
    testImplementation(libs.testing.mockWebServer)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
}