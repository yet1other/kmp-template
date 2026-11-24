package com.yet.convention

import org.gradle.api.Project

/** `app.basePackage` from the root gradle.properties (scripts/rename.sh changes it). */
internal val Project.basePackage: String
    get() = providers.gradleProperty("app.basePackage").get()

/**
 * The Android namespace of a module, derived from its Gradle path:
 * `:core:common` → `<base>.core.common`, `:feature:home` → `<base>.feature.home`, `:sharedLogic` → `<base>.sharedlogic`.
 */
internal val Project.derivedNamespace: String
    get() = basePackage + "." + path.removePrefix(":")
        .split(':')
        .joinToString(".") { it.replace("-", "").lowercase() }
