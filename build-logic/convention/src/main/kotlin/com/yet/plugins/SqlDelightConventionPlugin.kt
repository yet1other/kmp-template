package com.yet.plugins

import app.cash.sqldelight.gradle.SqlDelightExtension
import com.yet.convention.libs
import com.yet.convention.library
import com.yet.convention.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/**
 * `libs.plugins.local.sqlDelight`: SQLDelight for KMP modules (Android, iOS).
 * Every database is generated with async APIs, so a web worker driver (js/wasm) can be added later without
 * touching the queries; Android and iOS use the synchronous drivers through `Schema.synchronous()`.
 */
class SqlDelightConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(libs.pluginId("sqlDelight"))

        extensions.configure<SqlDelightExtension> {
            databases.configureEach {
                generateAsync.set(true)
            }
        }

        extensions.configure<KotlinMultiplatformExtension> {
            // The native driver binds to the system SQLite: the app links it through Xcode (OTHER_LDFLAGS),
            // native test executables need it explicitly.
            targets.withType<KotlinNativeTarget>().configureEach {
                binaries.configureEach { linkerOpts("-lsqlite3") }
            }

            sourceSets.apply {
                commonMain.dependencies {
                    implementation(libs.library("sqldelight-coroutines-extensions"))
                    implementation(libs.library("sqldelight-async-extensions"))
                }
                androidMain.dependencies {
                    implementation(libs.library("sqldelight-android"))
                }
                // Host (JVM) unit tests run against an in-memory JDBC SQLite driver.
                getByName("androidHostTest").dependencies {
                    implementation(libs.library("sqldelight-driver"))
                }
                nativeMain.dependencies {
                    implementation(libs.library("sqldelight-native"))
                }
            }
        }
    }
}
