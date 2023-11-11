pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Album Reader"
include(":app")
include(":common:core")
include(":common:testing-utilities")
include(":data")
include(":datasource:api")
include(":domain")
include(":presentation")
