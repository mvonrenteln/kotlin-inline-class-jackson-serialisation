package com.github.mvonrenteln.serialisation.custom

import com.fasterxml.jackson.databind.module.SimpleModule

class KotlinInlineClassModule : SimpleModule("kontlin-inline-class-module") {

    init {
        setSerializerModifier(SingleValueWrappingTypeSerializerModifier())
        setDeserializerModifier(KotlinInlineClassDeserializerModifier())
    }
}