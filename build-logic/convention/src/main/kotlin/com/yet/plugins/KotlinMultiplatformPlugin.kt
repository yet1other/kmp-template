package com.yet.plugins

import com.yet.convention.configureKotlinMultiplatform
import com.yet.convention.libs
import com.yet.convention.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** `libs.plugins.local.kotlin.multiplatform`: KMP + Android KMP library + serialization, all targets. */
class KotlinMultiplatformPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.pluginId("kotlinMultiplatform"))
            apply(libs.pluginId("androidMultiplatformLibrary"))
            apply(libs.pluginId("kotlin-serialization"))
        }

        extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
    }
}
