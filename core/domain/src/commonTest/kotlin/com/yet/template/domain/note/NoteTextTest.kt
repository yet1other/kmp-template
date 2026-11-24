package com.yet.template.domain.note

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NoteTextTest {
    @Test
    fun blankTextIsNothingToStore() {
        assertNull(NoteText.normalize("   \n"))
    }

    @Test
    fun textIsTrimmedAndCapped() {
        assertEquals("hi", NoteText.normalize("  hi "))
        assertEquals(NoteText.MAX_LENGTH, NoteText.normalize("a".repeat(NoteText.MAX_LENGTH + 10))!!.length)
    }
}
