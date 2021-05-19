import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DataClassTest {

    private val objectMapper = ObjectMapper().registerKotlinModule()

    @Test
    fun testSerialize() {
        val dataClass = DataClass(InlineClass("inline value"), InnerDataClass("data class value"))

        val json = objectMapper.writeValueAsString(dataClass)
        assertEquals("""{"inlineClass":"inline value","innerDataClass":{"value":"data class value"}}""",
            json)
    }

    @Test
    fun testDeserialize() {
        val json = """{"inlineClass":"inline value","innerDataClass":{"value":"data class value"}}"""

        val dataClassDeserialized = objectMapper.readValue(json, DataClass::class.java)
        val dataClass = DataClass(InlineClass("inline value"), InnerDataClass("data class value"))
        assertEquals(dataClass, dataClassDeserialized)

    }

    @Test
    fun `test deserialize with value class as object` () {
        val json = """{"inlineClass":{"value":"inline value"},"innerDataClass":{"value":"data class value"}}"""

        val dataClassDeserialized = objectMapper.readValue(json, DataClass::class.java)
        val dataClass = DataClass(InlineClass("inline value"), InnerDataClass("data class value"))
        assertEquals(dataClass, dataClassDeserialized)
    }
}