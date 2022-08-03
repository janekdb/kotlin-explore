package life

import kotlin.random.Random

private data class Position(val col: Int, val row: Int)

class CellOutOfRangeException(message: String) : Exception(message)

/**
 * top left = 0, 0
 * bottom right = 9 9
 *
 * The grid wraps around.
 * The right neighbour of the rightmost cell is the leftmost cell in the same row.
 * etc.
 */
class Grid(private val side: Int) {

    // N rows with each row N cols in size
    val cells = Array(side) { Array(side) { 0 } }

    private fun o(x: Int, y: Int) = Offset(x, y)

    private fun p(col: Int, row: Int) = Position(col, row)

    fun addGlider() {
        val offsets = Library.offsets(Library.GLIDER)
        setOffsets(offsets, 5, 5)
    }

    fun addGliderGun() {
        val offsets = Library.offsets(Library.GOSPER_GLIDER_GUN)
        setOffsets(offsets, 20, 20)
    }

    fun addCopperhead() {
        val offsets = Library.offsets(Library.COPPERHEAD)
        setOffsets(offsets, side / 2, side - 20)
    }

    fun addNoahsArk() {
        val offsets = Library.offsets(Library.NOAHS_ARK)
        setOffsets(offsets, side / 2, side / 2)
    }

    private fun setOffsets(offsets: Set<Offset>, originX: Int, originY: Int) {
        val outOfRange = offsets.filter {
            it.x + originX > side - 1 || it.y + originY > side - 1
        }
        if (!outOfRange.isEmpty()) {
            throw CellOutOfRangeException(
                "originX: $originX, originY: $originY, outOfRange: $outOfRange, offsets: $offsets"
            )
        }
        offsets.forEach {
            cells[it.y + originY].set(it.x + originX, 1)
        }
    }

    fun killAllCells() {
        for (row in 0 until side) {
            for (col in 0 until side) {
                cells[row].set(col, 0)
            }
        }
    }

    fun randomize() {
        val rand = Random(System.currentTimeMillis())
        (0 until side).forEach {
            cells[it] = Array(side) { rand.nextInt(2) }
        }
//        cells.forEach {
//            (0..it.size).forEach
//            it.set(1, rand.nextInt(2))
//        }
    }

    fun nextGeneration(): Grid {

        val next = Grid(side)

        for (row in 0 until side) {
            for (col in 0 until side) {
                val alive = calcAlive(row, col)
                val cell = if (alive) 1 else 0
                next.cells[row].set(col, cell)
            }
        }
        return next
    }

    /**
     * Rules:
     *
     * Any live cell with two or three live neighbours survives.
     * Any dead cell with three live neighbours becomes a live cell.
     * All other live cells die in the next generation. Similarly, all other dead cells stay dead.
     */
    private fun calcAlive(row: Int, col: Int): Boolean {
        val top = (row - 1 + side) % side
        val bottom = (row + 1) % side
        val left = (col - 1 + side) % side
        val right = (col + 1) % side

        val count =
            cells[top][left] + cells[top][col] + cells[top][right] +
                    cells[row][left] + cells[row][right] +
                    cells[bottom][left] + cells[bottom][col] + cells[bottom][right]

        val thisCell = cells[row][col]
        val nextState = if (thisCell == 1 && (count == 2 || count == 3))
            true
        else if (thisCell == 0 && count == 3)
            true
        else
            false
        return nextState
    }
}