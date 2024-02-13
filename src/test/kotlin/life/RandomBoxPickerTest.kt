package life

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class RandomBoxPickerTest {

    @Test
    fun emptyListRejected() {
        val picker = RandomBoxPicker()
        val boxes = emptyList<Box>()
        assertFailsWith<EmptyBoxListException>(
            block = { picker.pickBox(boxes) }
        )
    }

    @Test
    fun picksRandomly() {
        val picker = RandomBoxPicker()
        val boxes = listOf(
            Box(0, 0, 3, 3),
            Box(0, 0, 2, 2),
            Box(0, 0, 1, 1)
        )
        val frequency = mutableMapOf<Int, Int>()
        val pickedWidths = mutableSetOf<Int>()
        var iterations = 0
        while (pickedWidths.size < boxes.size || iterations <= 200) {
            val picked = picker.pickBox(boxes)
            val width = picked.width
            pickedWidths.add(width)
            frequency[width] = frequency.getOrDefault(width, 0) + 1
            if (iterations++ >= 400) {
                fail("Probably not random")
            }
        }
        /* Check wider boxes were picked more often */
        println("frequency: $frequency")
        assertEquals(setOf(2,3,4), frequency.keys)
        for (width in 2..3) {
            val lowerWidthCount = frequency[width]!!
            val higherWidthCount = frequency[width + 1]!!
            assertTrue(
                higherWidthCount > lowerWidthCount,
                "higherWidthCount: $higherWidthCount was not > lowerWidthCount: $lowerWidthCount"
            )
        }
    }
}