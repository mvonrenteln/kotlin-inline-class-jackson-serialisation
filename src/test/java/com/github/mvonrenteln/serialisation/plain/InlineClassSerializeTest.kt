package com.github.mvonrenteln.serialisation.plain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class InlineClassSerializeTest {

    private val objectMapper = ObjectMapper().registerKotlinModule()

    @Test
    fun `test serialize inline class`() {
        val dataClass = Name("Jon")

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals("""Jon""", json)
    }

    @Test
    fun `test deserialize inline class`() {
        val json = """{"value":"Jon"}"""

        val deserialized = objectMapper.readValue<Name>(json)
        assertEquals(Name("Jon"), deserialized)

    }

    @Test
    fun `test serialize data class with inline class`() {
        val dataClass = SimpleName(Name("Jon"))

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals("""{"name":"Jon"}""", json)
    }

    @Test
    fun `test deserialize data class with inline class`() {

        val json = """{"name":"Jon"}"""

        val deserialized = objectMapper.readValue<SimpleName>(json)
        assertEquals(SimpleName(Name("Jon")), deserialized)

    }

    @Test
    fun `test serialize data class containing inline class and data class`() {
        val dataClass = FullName(Name("Jon"), Surename("Snow"))

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals("""{"name":"Jon","surename":{"value":"Snow"}}""", json)
    }

    @Test
    fun `test deserialize data class containing inline class and data class`() {
        val json = """{"name":"Jon","surename":{"value":"Snow"}}"""

        val dataClassDeserialized = objectMapper.readValue<FullName>(json)
        val dataClass = FullName(Name("Jon"), Surename("Snow"))
        assertEquals(dataClass, dataClassDeserialized)

    }
}