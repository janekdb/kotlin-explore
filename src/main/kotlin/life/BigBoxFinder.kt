package life

import java.lang.IllegalArgumentException

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
     * Find a list of the largest square boxes that can be placed into the grid
     * without including a live cell. The list is sorted by box size with the largest first.
     *
     * Every returned box is at least of size [minimumSize].
     *
     * Algo details: A square grows down and to the right. By starting the top left in every
     * dead cell we are guaranteed to find all boxes. This is not an efficient
     * algorithm!
     *
     * Because the method finds the largest box that starts at a given top left each top left
     * point occurs a maximum of once in the returned squares.
     *
     * @param grid The grid to search for empty squares
     * @param minimumSize The minimum size for returned squares
     * @return A list of the largest square boxes that can be placed into the grid.
     */
    fun findBoxes(grid: Grid, minimumSize: Int = 1): List<Box> {
        val gridWidth = grid.width
        val gridHeight = grid.height
        if (gridHeight != gridWidth)
            throw Exception("Broken assumption: The grid is square. Update range check in growSquare to check row range in addition to column range")

        val gridSide = gridHeight
        if (minimumSize > gridSide)
            throw IllegalArgumentException("minimumSize must not be greater than the grid side: minimumSize: $minimumSize, grid side: $gridSide")
        if (minimumSize <= 0)
            throw IllegalArgumentException("minimumSize must be > 0: minimumSize: $minimumSize")

        /**
         * Skip row and cols indexes that are too large for a large enough square to be started in and
         * then need to grow down and to the right extending past the bottom or right of the grid
         */
        val lastPossibleStartingRow = gridHeight - minimumSize
        val lastPossibleStaringCol = gridWidth - minimumSize

        // TODO: Add tests to confirm start in dead cell
        val rows = 0..lastPossibleStartingRow
        val startingCells: List<Offset> = rows.flatMap { row ->
            val deadCells = grid.deadCellsForRow(row)
            deadCells.filter { it <= lastPossibleStaringCol }.map { col -> Offset(col, row) }
        }
        val squares = startingCells.map { topLeftDeadCell ->

            //start in dead cell
            val initialSquare = with(topLeftDeadCell) { Box(x, y, x, y) }
            val largestSquare = growSquare(initialSquare, grid.cells, gridHeight, gridWidth)
            largestSquare
        }
        val largeEnough = squares.filter { it.width >= minimumSize }
        val largestFirst = largeEnough.sortedByDescending { it.width }
        return largestFirst
    }

    /**
     * Attempt to grow the current square. If the square cannot be extended return the current square.
     * @param cells Cells indexed by row then column.
     */
    private fun growSquare(currentSquare: Box, cells: Array<Array<Int>>, height: Int, width: Int): Box {
        /* Grow right and down */
        var liveCellsInGrowZone = 0

        val newRightColumn = currentSquare.right + 1
        val newBottomRow = currentSquare.bottom + 1
        if (newRightColumn >= width || newBottomRow >= height)
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