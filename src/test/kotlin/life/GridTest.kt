package life

import org.junit.jupiter.api.Test
//import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    fun killAllCellsKillsAllCells() {
        val side = 10
        val grid = Grid(side)
        grid.addGlider()
        grid.killAllCells()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(0, liveCells)
    }

    @Test
    fun cellsOutOfRangeDetected() {
        val side = 2
        val grid = Grid(side)
        assertFailsWith(
            exceptionClass = CellOutOfRangeException::class,
            block = { grid.addGlider() }
        )
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
    private fun countLiveCells(cells: Array<Array<Int>>): Int {
        val rowCounts = cells.map { it.sum() }
        return rowCounts.sum()
    }
}