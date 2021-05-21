package com.github.mvonrenteln.serialisation.jmolecules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jmolecules.jackson.JMoleculesModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JmoleculesSerializeTest {

    private val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JMoleculesModule())

    @Test
    fun `test serialize inline class`() {
        val dataClass = Iban("Jon")

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals(""""Jon"""", json)
    }

    @Test
    fun `test deserialize inline class`() {
        val json = """{"value":"Jon"}"""

        val deserialized = objectMapper.readValue<Iban>(json)
        assertEquals(Iban("Jon"), deserialized)

    }

    @Test
    fun `test serialize data class with inline class`() {
        val dataClass = BankAccount(Iban("Jon"))

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals("""{"iban":"Jon"}""", json)
    }

    @Test
    fun `test deserialize data class with inline class`() {

        val json = """{"iban":"Jon"}"""

        val deserialized = objectMapper.readValue<BankAccount>(json)
        assertEquals(BankAccount(Iban("Jon")), deserialized)

    }
}