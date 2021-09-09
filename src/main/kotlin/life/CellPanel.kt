package life

import java.awt.Color
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

class CellPanel(private val cellsPerSide: Int) : JPanel() {

    val cellSize = 5

    private val cellDimension = Dimension(cellSize, cellSize)

    private var cells: Array<JPanel> = emptyArray()
    private val ages = IntArray(cellsPerSide * cellsPerSide) { 100 }

    init {
        val cellList = mutableListOf<JPanel>()
        layout = GridBagLayout()
        val colours = listOf(Color.GREEN, Color.BLACK, Color.YELLOW)
        val gbc = GridBagConstraints()
        gbc.gridy = 0
        for (col in 0 until cellsPerSide) {
            gbc.gridx = 0
            for (row in 0 until cellsPerSide) {
                val cell = object : JPanel() {
                    override fun getPreferredSize(): Dimension {
                        return cellDimension
                    }
                }
                // TODO: Why is this random?
                cell.background = colours.random() // colours[colour]
                add(cell, gbc)
                cellList.add(cell)
                gbc.gridx++
            }
            gbc.gridy++
        }
        cells = cellList.toTypedArray()
    }

    /**
     * @param status A value from 0 to 100. 100 means the cell is alive. For each generation the cell is dead
     * it's status is decremented to a minimum of 0.
     */
    fun setAliveStatus(row: Int, col: Int, status: Int) {
        val index = row * cellsPerSide + col
        val cell = cells[index]
        if (status == 100) {
            cell.background = Color.GREEN
            ages[index] = 0
        } else {
            val age = ages[index]
            if (age < 100) {
                ages[index]++
            }
            val component = 100 - age
            val colour = Color(0, component, 0)
            cell.background = colour
        }
    }
}
