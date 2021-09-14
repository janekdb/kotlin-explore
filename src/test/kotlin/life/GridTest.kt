package life

import kotlin.test.Test
import kotlin.test.assertEquals

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
        val side = 5
        val grid = Grid(side)
        grid.addGlider()
        grid.killAllCells()
        val liveCells = countLiveCells(grid.cells)
        assertEquals(0, liveCells)
    }

    private fun countLiveCells(cells: Array<Array<Int>>): Int {
        val rowCounts = cells.map { it.sum() }
        return rowCounts.sum()
    }
}