package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.mvonrenteln.serialisation.jmolecules.BankAccount
import com.github.mvonrenteln.serialisation.jmolecules.Iban
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KotlinInlineClassSerializeTest {

    private val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(KotlinInlineClassModule())

    @Test
    fun `test serialize inline class`() {
        val dataClass = Iban("12345")

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals(""""12345"""", json)
    }

    @Test
    fun `test deserialize inline class`() {
        val json = """"12345""""

        val deserialized = objectMapper.readValue<Iban>(json)
        assertEquals(Iban("12345"), deserialized)

    }

    @Test
    fun `test serialize data class with inline class`() {
        val dataClass = BankAccount(Iban("12345"))

        val json = objectMapper.writeValueAsString(dataClass)
        // compresses more than one child. wanted?
        assertEquals(""""12345"""", json)
    }

    @Test
    fun `test deserialize data class with inline class`() {

        val json = """{"iban":"12345"}"""

        val deserialized = objectMapper.readValue<BankAccount>(json)
        assertEquals(BankAccount(Iban("12345")), deserialized)

    }
}