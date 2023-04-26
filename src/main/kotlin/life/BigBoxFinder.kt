package life

/**
 * The rows and columns of the cells that define a box border constructed from cells.
 *
 * The width of the box is right - left + 1.
 * The height of the box is bottom - top + 1.
 */
data class Box(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    val width = right - left + 1
    val height = bottom - top + 1
}

/**
 * Given a Grid find the biggest unoccupied boxes. All boxes are squares.
 */
class BigBoxFinder {
    /**
     * @return A list of the largest square boxes that can be placed into the grid
     * without including a live cell.
     *
     * A square grows down and to the right. By starting the top left in every
     * dead cell we are guaranteed to find all boxes. This is not an efficient
     * algorithm!
     */
    fun findBoxes(grid: Grid): List<Box> {
        val gridWidth = grid.width
        val gridHeight = grid.height
        if (gridHeight != gridWidth)
            throw Exception("Update range check in growSquare to check row range in addition to column range")
        // TODO: Add tests to confirm start in dead cell
        val rows = 0 until gridHeight
        val startingCells = rows.flatMap { row ->
            val deadCells = grid.deadCellsForRow(row)
            deadCells.map { col -> Offset(col, row) }
        }
        val largestSquares = startingCells.map { topLeftDeadCell ->

            //start in dead cell
            val initialSquare = with(topLeftDeadCell) { Box(x, y, x, y) }
            val largestSquare = growSquare(initialSquare, grid.cells, gridHeight, gridWidth)
            largestSquare
        }
        val largestFirst = largestSquares.sortedBy { it.left - it.right }
        return largestFirst
    }

    /**
     * @param cells Cells indexed by row then column.
     */
    private fun growSquare(currentSquare: Box, cells: Array<Array<Int>>, height: Int, width: Int): Box {
        /* Grow right and down */
        var liveCellsInGrowZone = 0

        val newRightColumn = currentSquare.right + 1
        val newBottomRow = currentSquare.bottom + 1
        if (newRightColumn >= width|| newBottomRow >= height)
            return currentSquare

        for (row in currentSquare.top..currentSquare.bottom)
            liveCellsInGrowZone += cells[row][newRightColumn]

        for (col in currentSquare.left..currentSquare.right)
            liveCellsInGrowZone += cells[newBottomRow][col]

        liveCellsInGrowZone += cells[newBottomRow][newRightColumn]

        val result = if (liveCellsInGrowZone == 0)
            growSquare(
                Box(
                    currentSquare.left,
                    currentSquare.top,
                    newRightColumn,
                    newBottomRow
                ),
                cells,
                height,
                width
            )
        else
            currentSquare

        return result
    }
}