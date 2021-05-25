package com.github.mvonrenteln.serialisation.custom

/**
 * Checks if the class is an inline Kotlin class.
 * @return true if it's inline
 */
fun Class<*>.isInlineClass() = declaredMethods.any { it.name == "box-impl" }