package life

/**
 * John Conway's Game of life.Life
 */
class Life(private val lifeFrame: LifeFrame, cellsPerSide: Int) {
    private val grid = Grid(cellsPerSide)
    private val randomBoxPicker = RandomBoxPicker()
    private val patternAdder = PatternAdder(grid, randomBoxPicker)

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

    fun killAllCells() {
        grid.killAllCells()
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

//    grid is a var so maybe having Grid as a constructor param to PatternAdder is a poor choice
//    try changing var grid to val grid first

    fun addBlock() = patternAdder.addBlock()

    fun addDeadSparkCoil() = patternAdder.addDeadSparkCoil()

    fun addGlider() = patternAdder.addGlider()

    fun addGliderGun() = patternAdder.addGliderGun()

    fun addCopperhead() = patternAdder.addCopperhead()

    fun addNoahsArk() = patternAdder.addNoahsArk()

    fun addCrab() = patternAdder.addCrab()

    fun randomize() {
        grid.randomize()
    }

    fun step() {
        grid.nextGeneration()
    }
}
