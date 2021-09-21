package life.control

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

private const val KILL = "Kill All Cells"
private const val ADD_GLIDER = "Add Glider"
private const val ADD_GLIDER_GUN = "Add Glider Gun"
private const val ADD_COPPERHEAD = "Add Copperhead"
private const val ADD_NOAHS_ARK = "Add Noahs Ark"

private const val CONTROLS_WIDTH = 200
private const val CONTROLS_HEIGHT = 200

class ControlsFrame(
    private val killAction: () -> Unit,
    private val addGliderAction: () -> Unit,
    private val addGliderGunAction: () -> Unit,
    private val addCopperheadAction: () -> Unit,
    private val addNoahsArkAction: () -> Unit
) : JFrame() {

    init {
        create()
    }

    private fun create() {
        setSize(CONTROLS_WIDTH, CONTROLS_HEIGHT)
        title = "Controls"
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocation(100, 150)

        val contentPanel = JPanel()
        contentPanel.layout = null

        val panel = JPanel()
        panel.setBounds(10, 10, CONTROLS_WIDTH - 20, CONTROLS_HEIGHT - 20)
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        contentPanel.add(panel)

        panel.add(createButton(KILL, killAction))
        panel.add(createButton(ADD_GLIDER, addGliderAction))
        panel.add(createButton(ADD_GLIDER_GUN, addGliderGunAction))
        panel.add(createButton(ADD_COPPERHEAD, addCopperheadAction))
        panel.add(createButton(ADD_NOAHS_ARK, addNoahsArkAction))

        contentPane = contentPanel
    }

    private fun createButton(title: String, action: () -> Unit): JButton {
        val btn = JButton(title)
        btn.addActionListener { action() }
        return btn
    }
}