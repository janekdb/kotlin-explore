package life

/**
 * Class responsible for adding patterns to a grid
 */
class PatternAdder(private val grid: Grid, private val boxPicker: BoxPicker) {

    fun addBlock(): Boolean {
        val result = addPattern(Library.BLOCK)
        return result
    }

    fun addDeadSparkCoil(): Boolean {
        val result = addPattern(Library.DEAD_SPARK_COIL)
        return result
    }
    fun addGlider(): Boolean {
        val result = addPattern(Library.GLIDER)
        return result
    }

    fun addGliderGun(): Boolean {
        val result = addPattern(Library.GOSPER_GLIDER_GUN)
        return result
    }

    fun addCopperhead(): Boolean {
        val result = addPattern(Library.COPPERHEAD)
        return result
    }

    fun addNoahsArk(): Boolean {
        val result = addPattern(Library.NOAHS_ARK)
        return result
    }

    fun addCrab(): Boolean {
        val result = addPattern(Library.CRAB)
        return result
    }

    private fun addPattern(name: String): Boolean {
        val offsets = Library.offsets(name)
        val result = grid.addPattern(offsets, boxPicker)
        return result
    }
}