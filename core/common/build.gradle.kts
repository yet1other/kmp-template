// Cross-cutting helpers every module may use: dispatchers, logging, Decompose/MVIKotlin glue, common bindings.
plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.metro)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.decompose)
            implementation(libs.bundles.mvi)
            implementation(libs.kermit)
        }
    }
}
