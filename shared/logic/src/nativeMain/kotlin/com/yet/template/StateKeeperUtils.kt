package com.yet.template

import com.arkivanov.essenty.statekeeper.SerializableContainer
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.serialization.json.Json
import platform.Foundation.NSCoder
import platform.Foundation.NSString
import platform.Foundation.decodeTopLevelObjectOfClass
import platform.Foundation.encodeObject

private const val STATE_KEY = "state"

/** Writes the component tree's saved state into the system's restoration archive. */
fun save(coder: NSCoder, state: SerializableContainer) {
    coder.encodeObject(`object` = Json.encodeToString(SerializableContainer.serializer(), state), forKey = STATE_KEY)
}

/** The state written by [save], or null on a cold start or when it cannot be read. */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun restore(coder: NSCoder): SerializableContainer? =
    (coder.decodeTopLevelObjectOfClass(aClass = NSString, forKey = STATE_KEY, error = null) as String?)?.let {
        try {
            Json.decodeFromString(SerializableContainer.serializer(), it)
        } catch (e: Exception) {
            null
        }
    }
