package com.yet.template.common.decompose

import com.arkivanov.decompose.value.MutableValue
import kotlin.test.Test
import kotlin.test.assertEquals

class CombineTest {

    @Test
    fun emitsOnSubscribeAndOnEveryChange() {
        val a = MutableValue(1)
        val b = MutableValue("x")
        val seen = mutableListOf<String>()

        val cancellation = combine(a, b) { n, s -> "$s$n" }.subscribe { seen += it }
        a.value = 2
        b.value = "y"
        cancellation.cancel()
        a.value = 3

        assertEquals(listOf("x1", "x2", "y2"), seen)
    }
}
