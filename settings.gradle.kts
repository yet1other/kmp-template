rootProject.name = "KmpTemplate"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Apps
include(":androidApp")
project(":androidApp").projectDir = file("app/androidApp")

// The application graph (Metro) and the Compose UI shared by Android and iOS
include(":sharedLogic")
project(":sharedLogic").projectDir = file("shared/logic")
include(":sharedUI")
project(":sharedUI").projectDir = file("shared/ui")

// Core
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:resources")

// Features
include(":feature:root")
include(":feature:home")
