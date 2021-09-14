package life.control

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JFrame

private const val KILL = "Kill All Cells"

class ControlsFrame(private val killAction: () -> Unit) : JFrame() {

    init {
        create()
    }

    private fun create() {

        val killButton = JButton(KILL)
        killButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                println("Kill Button Clicked!")
                killAction()
            }
        })

        setTitle("Controls")
        defaultCloseOperation = EXIT_ON_CLOSE
        add(killButton)
        setSize(100, 200)
        pack()
        setLocation(100, 150)
    }
}