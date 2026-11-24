// The application graph (Metro): every module's bindings meet here, with the platform parts
// (Android Context, database drivers, HTTP engines) provided by AndroidAppGraph / NativeAppGraph.
plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.metro)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // What the UI renders.
            api(projects.core.domain)
            api(projects.feature.root)
            api(libs.bundles.decompose)
            // Not exposed: they contribute bindings to the graph, which provides their platform parts.
            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.database)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.sqldelight.driver)
            implementation(libs.mvikotlin)
        }
    }
}
