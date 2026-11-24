plugins {
    `kotlin-dsl`
}

group = "com.yet.buildlogic"

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "com.plugins.kotlinMultiplatformPlugin"
            implementationClass = "com.yet.plugins.KotlinMultiplatformPlugin"
        }
        register("composeMultiplatform") {
            id = "com.plugins.composeMultiplatform"
            implementationClass = "com.yet.plugins.ComposeMultiplatformPlugin"
        }
        register("feature") {
            id = "com.plugins.feature"
            implementationClass = "com.yet.plugins.FeaturePlugin"
        }
        register("sqlDelight") {
            id = "com.plugins.sqldelight"
            implementationClass = "com.yet.plugins.SqlDelightConventionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.sqldelight.gradlePlugin)
}
