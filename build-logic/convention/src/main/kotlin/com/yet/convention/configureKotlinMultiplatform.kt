package com.yet.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private const val JDK_VERSION = 17

/**
 * Targets and common dependencies of every KMP module: Android (KMP library plugin), iosArm64 and iosSimulatorArm64.
 * New targets (js, wasmJs, jvm desktop) go here, once, for every module.
 */
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension,
) = extension.apply {
    jvmToolchain(JDK_VERSION)

    extensions.configure<KotlinMultiplatformAndroidLibraryExtension>("android") {
        namespace = derivedNamespace
        compileSdk = libs.versionInt("android-compileSdk")
        minSdk = libs.versionInt("android-minSdk")
        // Android host (JVM) unit tests: `./gradlew testAndroidHostTest`.
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain.dependencies {
            implementation(libs.library("kotlinx-coroutines-core"))
            implementation(libs.library("kotlinx-datetime"))
            implementation(libs.library("kotlinx-serialization-json"))
        }
        commonTest.dependencies {
            implementation(libs.bundle("testing"))
        }
        // kotlin-test picks its JUnit flavour per compilation; name it for the Android host tests.
        getByName("androidHostTest").dependencies {
            implementation(libs.library("kotlin-testJunit"))
        }
    }
}
