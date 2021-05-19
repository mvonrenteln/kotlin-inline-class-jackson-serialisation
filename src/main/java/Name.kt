import java.time.LocalDate

@JvmInline
value class Name(val value: String)

data class Surename(val value: String)

data class SimpleName(val name: Name)

data class FullName(val name: Name, val surename: Surename)