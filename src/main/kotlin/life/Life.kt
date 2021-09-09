package life

/**
 * John Conway's Game of life.Life
 */
class Life(private val lifeFrame: LifeFrame, cellsPerSide: Int) {
    private var grid = Grid(cellsPerSide)

    fun display() {
        var rowIdx = 0
        grid.cells.forEach { row ->
            var colIdx = 0
            row.forEach { cell ->
                lifeFrame.cellPane.setAliveStatus(rowIdx, colIdx, cell * 100)
                colIdx++
            }
            rowIdx++
        }
    }

    fun displayPrint() {
        println("The Grid")
        grid.cells.forEach { row ->
            row.forEach { cell -> showCell(cell) }
            println()
        }
    }

    private val deadCell = '·'
    private val liveCell = '█'
    private fun showCell(alive: Int) {
        val char = if (alive == 1) liveCell else deadCell
        print(char)
    }

    fun addGlider() {
        grid.addGlider()
    }

    fun randomize() {
        grid.randomize()
    }

    fun step() {
        grid = grid.nextGeneration()
    }
}