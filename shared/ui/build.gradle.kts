// The Compose UI for Android and iOS. iOS links it as the static `SharedUI` framework
// (app/iosApp runs :sharedUI:embedAndSignAppleFrameworkForXcode).
plugins {
    alias(libs.plugins.local.kotlin.multiplatform)
    alias(libs.plugins.local.compose.multiplatform)
}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "SharedUI"
            isStatic = true
            binaryOption("bundleId", "${providers.gradleProperty("app.basePackage").get()}.sharedui")
        }
    }

    android {
        androidResources {
            enable = true
        }
    }

    compilerOptions {
        optIn.add("com.arkivanov.decompose.ExperimentalDecomposeApi")
        optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.sharedLogic)
            implementation(projects.core.resources)
            implementation(libs.decompose.compose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
    }
}

composeCompiler {
    // Domain and component models are immutable data classes: compare them by equality (see the file).
    stabilityConfigurationFiles.add(layout.projectDirectory.file("compose-stability.conf"))
}
