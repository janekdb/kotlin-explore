package life

import javax.swing.JFrame

class LifeFrame(title: String, private val cellsPerSide: Int) : JFrame() {

    val cellPane = CellPanel(cellsPerSide)

    init {
        createUI(title)
    }

    private fun createUI(title: String) {
        setTitle(title)
        defaultCloseOperation = EXIT_ON_CLOSE
        add(cellPane)
        setSize(cellsPerSide * cellPane.cellSize, cellsPerSide * cellPane.cellSize)
        pack()
        setLocationRelativeTo(null)
    }
}
