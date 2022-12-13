pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Ktor_SSE"
include(":androidApp")
include(":shared")
include(":backend")
include(":shared-models")
