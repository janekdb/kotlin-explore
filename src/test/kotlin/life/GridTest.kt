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
        val side = 10
        /* Leave a biggest box of 7x7. The glider is 3x3. Grid.addPattern
        * requires an empty margin of PATTERN_PLACEMENT_MARGIN around the placed
        * pattern */
        //
        // 0 ..........
        // 1 .xxx......
        // 2 ..x.......
        // 3 .x.......x
        // 4 ..........
        // 5 ..........
        // 6 ..........
        // 7 ..........
        // 8 ..........
        // 9 .........x
        val offsets = setOf(
            Offset(1, 1), Offset(2, 1), Offset(3, 1),
            Offset(2, 2),
            Offset(1, 3), Offset(9, 3),
            Offset(9, 9)
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
        val outerXs = 2..8
        val outerYs = 3..9
        val outerSquare = allOffsets(outerXs, outerYs)
        val innerXs = 4..6
        val innerYs = 5..7
        val innerSquare = allOffsets(innerXs, innerYs)
        val marginOffsets = outerSquare.minus(innerSquare)
        val marginLiveCellCount = countLiveCells(marginOffsets, grid)
        assertEquals(0, marginLiveCellCount)
        val innerSquareLiveCellCount = countLiveCells(innerSquare, grid)
        assertEquals(5, innerSquareLiveCellCount)
    }

    private fun countLiveCells(offsets: Set<Offset>, grid: Grid): Int {
        return offsets.sumOf { offset ->
            grid.cells[offset.y][offset.x]
        }
    }

    private fun allOffsets(xs: IntRange, ys: IntRange): Set<Offset> {
        val marginOffsets =
            xs.flatMap { x -> ys.map { y -> Offset(x, y) } }

        println(marginOffsets::class.simpleName)
        return marginOffsets.toSet()
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

        fun topBoxCentreCell(grid: Grid) = grid.cells[2][2]
        fun bottomBoxCentreCell(grid: Grid) = grid.cells[3][3]

        fun newGrid(): Grid {
            val side = 6
            /* Leave two 5x5 boxes. These are the biggest boxes */
            // .....x
            // ......
            // ......
            // ......
            // ......
            // x.....
            val offsets = setOf(
                Offset(5, 0), Offset(0, 5)
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