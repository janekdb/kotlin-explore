@file:JvmName("Main")

package life

import life.control.ControlsFrame
import java.awt.*
import kotlin.concurrent.fixedRateTimer

fun main() {
    EventQueue.invokeLater(::createAndShowUI)
}

private const val TITLE = "Kotlin life.Life"
private const val CELLS_PER_SIDE = 150

private fun createAndShowUI() {

    val life = createLife()

    life.display()
    life.addGlider()
    life.randomize()
    life.display()

    createControls(life)

    fixedRateTimer(
        name = "generation-timer",
        initialDelay = 0, period = 100
    ) {
        life.step()
        life.display()
    }

}

private fun createLife(): Life {
    val frame = LifeFrame(TITLE, CELLS_PER_SIDE)
    frame.isVisible = true
    return Life(frame, CELLS_PER_SIDE)
}

private fun createControls(life: Life) {
    val kill = {
        life.killAllCells()
        life.display()
    }
    val addGlider = {
        life.addGlider()
        life.display()
    }
    val frame = ControlsFrame(kill, addGlider)
    frame.isVisible = true
//    return Controls(frame)
}