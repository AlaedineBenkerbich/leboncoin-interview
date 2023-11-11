plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "fr.leboncoin.albumreader.di"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.google.hilt.android)
    kapt(libs.google.hilt.compiler)
}

hilt {
    enableAggregatingTask = true
}