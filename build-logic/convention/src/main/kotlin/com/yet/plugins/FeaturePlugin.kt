package com.yet.plugins

import com.yet.convention.bundle
import com.yet.convention.libs
import com.yet.convention.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * `libs.plugins.local.feature`: a feature module. Decompose components, MVIKotlin stores, mappers and Metro
 * bindings; no UI (that lives in `:sharedUI`). Features see `:core:domain` and the helpers in `:core:common`;
 * data is wired only by the application graph in `:sharedLogic`.
 */
class FeaturePlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(KotlinMultiplatformPlugin::class.java)
        pluginManager.apply(libs.pluginId("metro"))

        extensions.configure<KotlinMultiplatformExtension> {
            compilerOptions {
                optIn.add("com.arkivanov.decompose.ExperimentalDecomposeApi")
                optIn.add("kotlinx.serialization.ExperimentalSerializationApi")
            }

            sourceSets.apply {
                commonMain.dependencies {
                    // Typesafe `projects` accessors exist for build scripts only, not for build-logic.
                    api(project(":core:domain"))
                    implementation(project(":core:common"))

                    implementation(libs.bundle("decompose"))
                    implementation(libs.bundle("mvi"))
                }
            }
        }
    }
}
