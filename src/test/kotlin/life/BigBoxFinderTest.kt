package life

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

// TODO: Account for wrap around
internal class BigBoxFinderTest {

    @Test
    fun findsBoxesWhenGridIsEmpty() {
        val side = 3
        val grid = Grid(side)
        val finder = BigBoxFinder()
        val foundBoxes = finder.findBoxes(grid)
        assertEquals(9, foundBoxes.size)
        val actualBox = foundBoxes[0]
        assertEquals(0, actualBox.left)
        assertEquals(0, actualBox.top)
        assertEquals(2, actualBox.right)
        assertEquals(2, actualBox.bottom)
    }

    /*
    * Grid:
    *   000
    *   000
    *   001
    *
    * Largest box:
    *   XX0
    *   XX0
    *   001
     */
    @Test
    fun findsBoxesWhenRightBottomCellAlive() {
        val side = 3
        val initialCells = setOf(Offset(2, 2))
        val grid = Grid(side, initialCells)
        val finder = BigBoxFinder()
        val foundBoxes = finder.findBoxes(grid)
        assertEquals(8, foundBoxes.size)
        val actual = foundBoxes[0]
        val expected = Box(0, 0, 1, 1)
        assertEquals(expected, actual)
    }


    /*
    * Grid:
    *   100
    *   000
    *   001
    *
    * Largest boxes:
    *   1XX
    *   0XX
    *   001
    *
    *   100
    *   XX0
    *   XX1
     */
    @Test
    fun findsLargestBoxesFirstWhenTopLeftBottomRightCellsAlive() {
        val side = 3
        val initialCells = setOf(Offset(0, 0), Offset(2, 2))
        val grid = Grid(side, initialCells)
        printGrid(grid)
        val finder = BigBoxFinder()
        val foundBoxes = finder.findBoxes(grid)
        assertEquals(7, foundBoxes.size)
        run {
            val actual = foundBoxes[0]
            val expected = Box(1, 0, 2, 1)
            assertEquals(expected, actual)
        }
        run {
            val actual = foundBoxes[1]
            val expected = Box(0, 1, 1, 2)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun findsNoBoxesWhenAllCellsAreAlive() {
        val side = 4
        val initialCells = (0 until side).flatMap { x ->
            (0 until side).map { y ->
                Offset(x, y)
            }
        }.toSet()
        val grid = Grid(side, initialCells)
        printGrid(grid)
        val finder = BigBoxFinder()
        val foundBoxes = finder.findBoxes(grid)
        assertTrue(foundBoxes.isEmpty())
    }

    /**

    found box:Box(left=0, top=0, right=2, bottom=2)
    found box:Box(left=1, top=0, right=2, bottom=1)
    found box:Box(left=0, top=1, right=1, bottom=2)

     * xxx
     * xxx
     * xxx
     *
     * .xx
     * .xx
     * ...
     *
     * ...
     * xx.
     * xx.
     *
    found box:Box(left=1, top=1, right=2, bottom=2)
    found box:Box(left=2, top=0, right=2, bottom=0)
    found box:Box(left=2, top=1, right=2, bottom=1)

     *
     * ...
     * .xx
     * .xx
     *
     * ..x
     * ...
     * ...
     *
     * ...
     * ..x
     * ...

    found box:Box(left=0, top=2, right=0, bottom=2)
    found box:Box(left=1, top=2, right=1, bottom=2)
    found box:Box(left=2, top=2, right=2, bottom=2)

     *
     * ...
     * ...
     * x..
     *
     * ...
     * ...
     * .x.
     *
     * ...
     * ...
     * ..x
     *
     *
     */

    /**

    These are the boxes that should be found

    found box:Box(left=0, top=0, right=2, bottom=2)
    found box:Box(left=0, top=0, right=1, bottom=1)
    found box:Box(left=1, top=0, right=2, bottom=1)
    found box:Box(left=0, top=1, right=1, bottom=2)
    found box:Box(left=1, top=1, right=2, bottom=2)

     * xxx
     * xxx
     * xxx
     *
     * xx.
     * xx.
     * ...
     *
     * .xx
     * .xx
     * ...
     *
     * ...
     * xx.
     * xx.
     *
     * ...
     * .xx
     * .xx
     *
     */
    @Test
    fun findsBoxesWhenGridIsEmptyWithMinimumRequiredBoxSize() {
        val side = 3
        val grid = Grid(side)
        val finder = BigBoxFinder()
        val minimumSize = 2
        val foundBoxes = finder.findBoxes(grid, minimumSize)
//        assertEquals(5, foundBoxes.size)
        for (box in foundBoxes) {
            println("found box: " + box + ": width: ${box.width}")
        }
        /* Don't assume we have squares */
        val unexpectedSmallBoxes = foundBoxes.filter { box -> box.width < minimumSize || box.height < minimumSize }
        assertTrue(
            unexpectedSmallBoxes.isEmpty(),
            "No boxes were smaller than the minimum size ($minimumSize): " + unexpectedSmallBoxes
        )
        val expectedBoxes = setOf(
            Box(left = 0, top = 0, right = 2, bottom = 2),
            Box(left = 0, top = 0, right = 1, bottom = 1),
            Box(left = 1, top = 0, right = 2, bottom = 1),
            Box(left = 0, top = 1, right = 1, bottom = 2),
            Box(left = 1, top = 1, right = 2, bottom = 2)
        )
        val actualBoxes = foundBoxes.toSet()
        val unexpectedActualBoxes = actualBoxes - expectedBoxes
        assertTrue(unexpectedActualBoxes.isEmpty(), "Unexpected actual boxes: " + unexpectedActualBoxes)
        assertEquals(expectedBoxes, actualBoxes)
    }

    @Test
    fun findsBoxesRejectsOutOfBoundsMinimumSize() {
        val side = 3
        val grid = Grid(side)
        val finder = BigBoxFinder()
        assertFailsWith<IllegalArgumentException>(
            message = "minimumSize must not be greater than the grid side: minimumSize: 4, grid side: 3",
            block = { finder.findBoxes(grid, 4) }
        )
        assertFailsWith<IllegalArgumentException>(
            message = "minimumSize must be > 0",
            block = { finder.findBoxes(grid, 0) }
        )
    }

    private fun printGrid(grid: Grid) {
        val cells = grid.cells
        for (row in 0 until grid.height) {
            for (col in 0 until grid.width) {
                print(cells[row][col])
            }
            println()
        }
    }

}