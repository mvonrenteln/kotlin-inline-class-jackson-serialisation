package com.github.mvonrenteln.serialisation.plain

import com.fasterxml.jackson.annotation.JsonCreator

@JvmInline
value class Name(val value: String)

data class Surename (val value: String)
// these so not work as well:
//data class Surename @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(val value: String)
//ata class Surename @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(val value: String)
// doesn't work with creator function as well

data class SimpleName (val name: Name) {

    // Creator function (comment on #413 in jackson-module-kotlin) works for deserializing inline classes. But this is ugly...
    companion object {
        @JsonCreator
        @JvmStatic
        fun create(name: String) = SimpleName(Name(name))
    }
}

data class FullName(val name: Name, val surename: Surename)