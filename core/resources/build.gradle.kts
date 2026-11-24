// Shared strings, images and the app theme (Compose Multiplatform resources): one source for Android and iOS.
plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.local.compose.multiplatform)
}

kotlin {
    android {
        androidResources {
            enable = true
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "${providers.gradleProperty("app.basePackage").get()}.resources"
    generateResClass = always
}
