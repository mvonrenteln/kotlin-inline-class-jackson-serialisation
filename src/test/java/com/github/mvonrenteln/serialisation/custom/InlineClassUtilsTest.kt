package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.annotation.JsonValue
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class InlineClassUtilsTest {

    @Test
    fun testIsInline() {
        assertTrue(Inline::class.java.isInlineClass())
    }
}

@JvmInline
private value class Inline(@JsonValue val value: Long)