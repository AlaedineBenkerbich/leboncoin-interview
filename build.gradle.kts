// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.plugin.android)
        classpath(libs.plugin.hilt)
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.kotlinSerialization)
        classpath(libs.plugin.navigationSafeArgs)
        classpath(libs.plugin.unMock)
    }
}