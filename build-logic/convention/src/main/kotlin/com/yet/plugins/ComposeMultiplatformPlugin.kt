package com.yet.plugins

import com.yet.convention.libs
import com.yet.convention.library
import com.yet.convention.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * `libs.plugins.local.compose.multiplatform`: the Compose plugin and compiler with runtime, foundation, ui,
 * material3, adaptive and resources. Apply it next to `local.kotlin.multiplatform` (it does not add targets).
 */
class ComposeMultiplatformPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.pluginId("composeMultiplatform"))
            apply(libs.pluginId("composeCompiler"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonMain.dependencies {
                    implementation(libs.library("compose-runtime"))
                    implementation(libs.library("compose-foundation"))
                    implementation(libs.library("compose-ui"))
                    implementation(libs.library("compose-material3"))
                    implementation(libs.library("compose-material3-adaptive"))
                    implementation(libs.library("compose-components-resources"))
                    implementation(libs.library("compose-uiToolingPreview"))
                }
                androidMain.dependencies {
                    implementation(libs.library("compose-uiTooling"))
                }
            }
        }
    }
}
