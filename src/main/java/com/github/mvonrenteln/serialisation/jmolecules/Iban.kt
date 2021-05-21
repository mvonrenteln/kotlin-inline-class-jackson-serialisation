package com.github.mvonrenteln.serialisation.jmolecules

import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
@JvmInline
value class Iban(val value: String)

@ValueObject
data class BankAccount(val iban: Iban)