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

    private fun countLiveCells(cells: Array<Array<Int>>): Int {
        val rowCounts = cells.map { it.sum() }
        return rowCounts.sum()
    }
}