package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * https://github.com/FasterXML/jackson-module-kotlin/issues/199#issuecomment-450650440
 *
 * Doesn't work because "box-impl" is synthetic
 * and "InlineAnnotationIntrospector" will never be called for it.
 */
class KotlinAnnotationIntrospectorTest {
    @Test
    fun `Read and write inline classes with Jackson`() {
        val mapper = ObjectMapper().registerKotlinModule().registerModule(InlineModule)
        val watt = Watt(1234)
        val json = mapper.writeValueAsString(watt)
        assertEquals("1234", json)
        val result = mapper.readValue<Watt>(json)
        assertEquals(watt, result)
    }
}

@JvmInline
private value class Watt(@JsonValue val value: Long)



private object InlineModule : SimpleModule("Inline") {
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        context.appendAnnotationIntrospector(InlineAnnotationIntrospector)
    }

    object InlineAnnotationIntrospector : NopAnnotationIntrospector() {
        override fun findCreatorAnnotation(config: MapperConfig<*>, a: Annotated): JsonCreator.Mode? {
            if (a is AnnotatedMethod && a.name == "box-impl") {
                return JsonCreator.Mode.DEFAULT
            }
            return null
        }
    }
}