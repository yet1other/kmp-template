package com.yet.template.domain.note

import kotlin.jvm.JvmInline
import kotlin.time.Instant

@JvmInline
value class NoteId(val value: Long)

/** The sample entity: replace it with your own domain. */
data class Note(
    val id: NoteId,
    val text: String,
    val createdAt: Instant,
)

/** Rules a note's text must follow, shared by the UI (enable "Add") and the repository (reject). */
object NoteText {
    const val MAX_LENGTH = 500

    /** The text to store, or null when there is nothing to store. */
    fun normalize(raw: String): String? = raw.trim().takeIf { it.isNotEmpty() }?.take(MAX_LENGTH)
}
