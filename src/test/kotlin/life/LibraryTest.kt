package life

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LibraryTest {

    private fun o(x: Int, y: Int) = Offset(x, y)

    @Test
    fun definitionLoaded() {
        val offsets = Library.offsets("test-definition")
        val expected = setOf(o(1, 0), o(2, 1), o(0, 2), o(1, 2), o(2, 2))
        assertEquals(expected, offsets)
    }

    @Test
    fun gliderOffsetsAreProvided() {
        val offsets = Library.offsets(Library.GLIDER)
        val expected = setOf(o(1, 0), o(2, 1), o(0, 2), o(1, 2), o(2, 2))
        assertEquals(expected, offsets)
    }
}