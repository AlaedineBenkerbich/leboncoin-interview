plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "fr.leboncoin.albumreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.leboncoin.albumreader"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
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
    implementation(project(":presentation"))

    implementation(libs.google.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    kapt(libs.google.hilt.compiler)
}

hilt {
    enableAggregatingTask = true
}