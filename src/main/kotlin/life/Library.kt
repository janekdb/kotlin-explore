package life

import java.lang.IllegalStateException

data class Offset(val x: Int, val y: Int)

object Library {

    val GLIDER = "glider"

    // https://en.wikipedia.org/wiki/Gun_(cellular_automaton)
    val GOSPER_GLIDER_GUN = "gosper-glider-gun"

    //https://conwaylife.com/wiki/Copperhead
    val COPPERHEAD = "copperhead"

    // https://conwaylife.com/wiki/Noah%27s_ark
    val NOAHS_ARK = "noahs-ark"

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