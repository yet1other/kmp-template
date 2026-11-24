plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.local.sqlDelight)
    alias(libs.plugins.metro)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("${providers.gradleProperty("app.basePackage").get()}.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }
    }
}
