package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.Throws
import java.io.IOException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import org.jmolecules.ddd.annotation.ValueObject
import org.jmolecules.ddd.types.Identifier
import java.lang.Exception
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

/**
 * [BeanDeserializerModifier] to use a static factory method named `of` on single-attribute
 * [ValueObject]s and [Identifier]s.
 *
 * @author Oliver Drotbohm
 */
internal class KotlinInlineClassDeserializerModifier : BeanDeserializerModifier() {
    /*
	 * (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.deser.BeanDeserializerModifier#modifyDeserializer(com.fasterxml.jackson.databind.DeserializationConfig, com.fasterxml.jackson.databind.BeanDescription, com.fasterxml.jackson.databind.JsonDeserializer)
	 */
    override fun modifyDeserializer(
        config: DeserializationConfig, descriptor: BeanDescription,
        deserializer: JsonDeserializer<*>?
    ): JsonDeserializer<*> {
        val type = descriptor.beanClass
        if (type.isInlineClass()) {
            val parameterType = descriptor.findProperties()[0].primaryType
            return ValueClassDeserializer(type.kotlin, parameterType)
        }
        return super.modifyDeserializer(config, descriptor, deserializer)
    }
}




private class ValueClassDeserializer(val kClass: KClass<out Any>, val parameterType: JavaType) : StdDeserializer<Any>(
    Any::class.java
) {

    /*
     * (non-Javadoc)
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Any {
        val deserializer = context.findNonContextualValueDeserializer(parameterType)
        val nested = deserializer.deserialize(parser, context)
        return try {
            kClass.primaryConstructor!!.call(nested)
        } catch (o_O: Exception) {
            throw JsonParseException(parser, String.format("Failed to invoke primary constructor!", kClass), o_O)
        }
    }

    companion object {
        private const val serialVersionUID = -874251080013013301L
    }

}