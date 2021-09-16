package life

data class Offset(val x: Int, val y: Int)

object Library {

    val GLIDER = """
        .X.
        ..X
        XXX
    """.trimIndent()

    // https://en.wikipedia.org/wiki/Gun_(cellular_automaton)
    val GOSPER_GLIDER_GUN = """
        ........................X...........
        ......................X.X...........
        ............XX......XX............XX
        ...........X...X....XX............XX
        XX........X.....X...XX..............
        XX........X...X.XX....X.X...........
        ..........X.....X.......X...........
        ...........X...X....................
        ............XX......................
    """.trimIndent()

    fun offsets(definition: String): Set<Offset> {
        val rows = definition.lines()
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
}