package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import org.springframework.core.annotation.AnnotatedElementUtils
import com.fasterxml.jackson.databind.introspect.AnnotatedMember
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.Throws
import java.io.IOException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.jmolecules.ddd.annotation.ValueObject
import org.jmolecules.ddd.types.Identifier

/**
 * [BeanSerializerModifier] to serialize properties that are [ValueObject]s which in turn only carry a
 * single attribute as just that attribute.
 *
 * @author Oliver Drotbohm
 */
internal class SingleValueWrappingTypeSerializerModifier : BeanSerializerModifier() {
    /*
	 * (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ser.BeanSerializerModifier#modifySerializer(com.fasterxml.jackson.databind.SerializationConfig, com.fasterxml.jackson.databind.BeanDescription, com.fasterxml.jackson.databind.JsonSerializer)
	 */
    override fun modifySerializer(
        config: SerializationConfig, description: BeanDescription,
        serializer: JsonSerializer<*>?
    ): JsonSerializer<*> {
        val properties = description.findProperties()
        if (properties.size != 1) {
            return super.modifySerializer(config, description, serializer)
        }
        val type = description.beanClass
        return if (AnnotatedElementUtils.hasAnnotation(type, ValueObject::class.java)
            || Identifier::class.java.isAssignableFrom(type)
        ) {
            SingleAttributeSerializer(properties[0].accessor)
        } else super.modifySerializer(
            config,
            description,
            serializer
        )
    }

    private class SingleAttributeSerializer(private val member: AnnotatedMember) : StdSerializer<Any>(
        Any::class.java
    ) {
        /*
		 * (non-Javadoc)
		 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
		 */
        @Throws(IOException::class)
        override fun serialize(value: Any?, gen: JsonGenerator, provider: SerializerProvider) {
            val nested = member.getValue(value)
            provider.findValueSerializer(nested.javaClass).serialize(nested, gen, provider)
        }

        companion object {
            private const val serialVersionUID = 3242761376607559434L
        }
    }
}