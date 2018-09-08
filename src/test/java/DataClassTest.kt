import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DataClassTest {

    @Test
    fun testSerializeDataClass() {
        val dataClass = DataClass(InlineClass("inline value"), InnerDataClass("data class value"))

        assertEquals("DataClass(inlineClass=InlineClass(value=inline value), innerDataClass=InnerDataClass(value=data class value))",
            dataClass.toString())

        val json = ObjectMapper().registerKotlinModule().writeValueAsString(dataClass)
        assertEquals("""{"inlineClass":"inline value","innerDataClass":{"value":"data class value"}}""",
            json)
    }
}