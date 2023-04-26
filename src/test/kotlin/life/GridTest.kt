package life

import org.junit.jupiter.api.Test
//import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

internal class GridTest {

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
        grid.addGlider()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(5, liveCells)
    }

    @Test
    fun addedGliderIsCentredInBiggestBox(){
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
            Offset(7, 7))
        val grid = Grid(side, offsets)
        printGrid(grid)
        println()
        grid.addGlider()
        printGrid(grid)
        val liveCells = countLiveCells(grid.cells)
        assertEquals(5 + offsets.size, liveCells)
        /* Assert an empty margin around the glider */
        /* Assert the glider fills the centre */
        val xs = 2 .. 6
        val ys = 3 .. 5
        val marginOffsets =
            xs.map {x-> Offset(x, 2)} +
                    ys.map{y -> Offset(2, y)} +
                    xs.map{x -> Offset(x, 6)} +
                    ys.map{y -> Offset(6, y)}
        val marginLiveCellCount = marginOffsets.map {
            offset ->
            grid.cells[offset.y][offset.x]
        }.sum()
        assertEquals(0, marginLiveCellCount)
            println(marginOffsets)
    }
    @Test
    fun killAllCellsKillsAllCells() {
        val side = 10
        val grid = Grid(side)
        grid.addGlider()
        grid.killAllCells()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(0, liveCells)
    }

    @Test
    fun noErrorWhenGridTooNarrowForWideShape(){
        val side = 9
        val grid = Grid(side)
        val actual = grid.addGliderGun()
        assertFalse(actual)
    }

    @Test
    fun noErrorWhenGridTooShortForTallShape(){
        val side = 8
        val grid = Grid(side)
        val actual = grid.addCopperhead()
        assertFalse(actual)
    }

    @Test
    fun constructorOffsetsApplied(){
        val side = 5
        val offsets = setOf(Offset(0, 2), Offset(4, 4))
        val grid = Grid(side, offsets)
        val liveCells = countLiveCells(grid.cells)
        assertEquals(2, liveCells)
    }

    @Test
    fun deadCellsForRowIsCorrect(){
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