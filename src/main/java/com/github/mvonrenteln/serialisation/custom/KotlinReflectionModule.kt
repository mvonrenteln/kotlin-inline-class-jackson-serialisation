package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * A Jackson [Module] to support JMolecules' [ValueObject] and [Identifier] types.
 *
 * @author Oliver Drotbohm
 */
class KotlinReflectionModule : SimpleModule("kontlin-reflection-module") {

    init {
        setSerializerModifier(SingleValueWrappingTypeSerializerModifier())
        setDeserializerModifier(KotlinSingleValueWrappingTypeDeserializerModifier())
    }
}