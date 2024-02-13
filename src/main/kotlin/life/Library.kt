package life

import java.lang.IllegalStateException

data class Offset(val x: Int, val y: Int)

object Library {

    const val BLOCK = "block"

    const val DEAD_SPARK_COIL = "dead-spark-coil"

    const val GLIDER = "glider"

    // https://en.wikipedia.org/wiki/Gun_(cellular_automaton)
    const val GOSPER_GLIDER_GUN = "gosper-glider-gun"

    //https://conwaylife.com/wiki/Copperhead
    const val COPPERHEAD = "copperhead"

    // https://conwaylife.com/wiki/Noah%27s_ark
    const val NOAHS_ARK = "noahs-ark"

    // https://www.conwaylife.com/wiki/Crab
    const val CRAB = "crab"

    fun offsets(name: String): Set<Offset> {
        val rows = loadDefinition(name)
        val result = rows.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, char ->
                Pair(colIndex, char)
            }.filter {
                it.second == 'X'
            }.map {
                val colIndex = it.first
                Offset(colIndex, rowIndex)
            }
        }
        return result.flatten().toSet()
    }

    private fun loadDefinition(name: String): List<String> {
        val definitionText = javaClass.getResource("/definition/$name.def")?.readText()
            ?: throw IllegalStateException("Definition missing: $name.def")
        return definitionText.lines()
    }
}