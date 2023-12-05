package life

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

internal class GridTest {

    class DefaultBoxPicker : BoxPicker {
        override fun pickBox(boxes: List<Box>): Box {
            return boxes.first()
        }
    }

    private val defaultBoxPicker = DefaultBoxPicker()

    @Test
    fun newGridIsEmpty() {
        val side = 3
        val grid = Grid(side)
        val liveCells = countLiveCells(grid.cells)
        assertEquals(0, liveCells)
    }

    @Test
    fun addingGliderIncreasesLiveCells() {
        val side = 10
        val grid = Grid(side)
        val patternAdder = PatternAdder(grid, defaultBoxPicker)
        patternAdder.addGlider()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(5, liveCells)
    }

    @Test
    fun addedGliderIsCentredInBiggestBox() {
        val side = 8
        /* Leave a biggest box of 5x5 */
        // ........
        // .xxx....
        // ........
        // .x......
        // ........
        // ........
        // ........
        // .......x
        val offsets = setOf(
            Offset(1, 1), Offset(2, 1), Offset(3, 1),
            Offset(1, 3), Offset(7, 3),
            Offset(7, 7)
        )
        val grid = Grid(side, offsets)
//        printGrid(grid)
//        println()
        val glider = Library.offsets(Library.GLIDER)
        grid.addPattern(glider, defaultBoxPicker)
//        printGrid(grid)
//        println()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(5 + offsets.size, liveCells)
        /* Assert an empty margin around the glider */
        /* Assert the glider fills the centre */
        val xs = 2..6
        val ys = 3..5
        val marginOffsets =
            xs.map { x -> Offset(x, 2) } +
                    ys.map { y -> Offset(2, y) } +
                    xs.map { x -> Offset(x, 6) } +
                    ys.map { y -> Offset(6, y) }
        val marginLiveCellCount = marginOffsets.map { offset ->
            grid.cells[offset.y][offset.x]
        }.sum()
        assertEquals(0, marginLiveCellCount)
//        println(marginOffsets)
    }

    @Test
    fun addedGliderIsPlacedAlternatelyInCandidateBoxes() {
        class CyclicBoxPicker : BoxPicker {
            private var nextIndex = 0
            override fun pickBox(boxes: List<Box>): Box {
                return boxes[nextIndex++ % boxes.size]
            }
        }

        val cyclicBoxPicker = CyclicBoxPicker()

        fun topBoxCentreCell(grid: Grid) = grid.cells[1][1]
        fun bottomBoxCentreCell(grid: Grid) = grid.cells[2][2]

        fun newGrid(): Grid {
            val side = 4
            /* Leave two 3x3 boxes. These are the biggest boxes */
            // ...x
            // ....
            // ....
            // x...
            val offsets = setOf(
                Offset(3, 0), Offset(0, 3)
            )
            val grid = Grid(side, offsets)
            //printGrid(grid)
            //println()
            assertEquals(0, topBoxCentreCell(grid))
            assertEquals(0, bottomBoxCentreCell(grid))
            return grid
        }

        val dot = setOf(Offset(0, 0))

        fun checkDotPlacement(cellAccessor: (Grid) -> Int) {
            val grid = newGrid()
            grid.addPattern(dot, cyclicBoxPicker)
//            printGrid(grid)
//            println()
            assertEquals(1, cellAccessor(grid))
        }

        // assert the dot is added at (1, 1)
        checkDotPlacement(::topBoxCentreCell)
        // assert the dot is added at (2, 2)
        checkDotPlacement(::bottomBoxCentreCell)

        checkDotPlacement(::topBoxCentreCell)
        checkDotPlacement(::bottomBoxCentreCell)
    }

    @Test
    fun killAllCellsKillsAllCells() {
        val side = 10
        val grid = Grid(side)
        val patternAdder = PatternAdder(grid, defaultBoxPicker)
        patternAdder.addGlider()
        grid.killAllCells()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(0, liveCells)
    }

    @Test
    fun noErrorWhenGridTooNarrowForWideShape() {
        val side = 9
        val grid = Grid(side)
        val patternAdder = PatternAdder(grid, defaultBoxPicker)
        val wasAdded = patternAdder.addGliderGun()
        assertFalse(wasAdded)
    }

    @Test
    fun noErrorWhenGridTooShortForTallShape() {
        val side = 8
        val grid = Grid(side)
        val patternAdder = PatternAdder(grid, defaultBoxPicker)
        val wasAdded = patternAdder.addCopperhead()
        assertFalse(wasAdded)
    }

    @Test
    fun constructorOffsetsApplied() {
        val side = 5
        val offsets = setOf(Offset(0, 2), Offset(4, 4))
        val grid = Grid(side, offsets)
        val liveCells = countLiveCells(grid.cells)
        assertEquals(2, liveCells)
    }

    @Test
    fun deadCellsForRowIsCorrect() {
        val side = 3
        // .X.
        // X.X
        // XX.
        val offsets = setOf(
            Offset(1, 0),
            Offset(0, 1), Offset(2, 1),
            Offset(0, 2), Offset(1, 2)
        )
        val grid = Grid(side, offsets)
        assertEquals(listOf(0, 2), grid.deadCellsForRow(0))
        assertEquals(listOf(1), grid.deadCellsForRow(1))
        assertEquals(listOf(2), grid.deadCellsForRow(2))
    }

    @Test
    fun rowOutOfRangeDetected() {
        val side = 2
        val grid = Grid(side)
        assertFailsWith(
            exceptionClass = RowOutOfRangeException::class,
            block = { grid.deadCellsForRow(side * 2) }
        )
    }

    private fun countLiveCells(cells: Array<Array<Int>>): Int {
        val rowCounts = cells.map { it.sum() }
        return rowCounts.sum()
    }

    private fun printGrid(grid: Grid) {
        val symbols = arrayOf("\u00b7", "\u2588")
        val cells = grid.cells
        for (row in 0 until grid.height) {
            for (col in 0 until grid.width) {
                print(symbols[cells[row][col]])
            }
            println()
        }
    }

}