@file:JvmName("Main")

package life

import life.control.ControlsFrame
import java.awt.*
import kotlin.concurrent.fixedRateTimer

fun main() {
    println("Kotlin version: ${KotlinVersion.CURRENT}")
    EventQueue.invokeLater(::createAndShowUI)
}

private const val TITLE = "Kotlin Life"
private const val CELLS_PER_SIDE = 150

private fun createAndShowUI() {

    val life = createLife()

    life.display()
    life.randomize()
    life.display()

    createControls(life)

    fixedRateTimer(
        name = "generation-timer",
        initialDelay = 0, period = 50
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
    val addBlock = {
        life.addBlock()
        life.display()
    }
    val addDeadSparkCoil = {
        life.addDeadSparkCoil()
        life.display()
    }
    val addGlider = {
        life.addGlider()
        life.display()
    }
    val addGliderGun = {
        life.addGliderGun()
        life.display()
    }
    val addCopperhead = {
        life.addCopperhead()
        life.display()
    }
    val addNoahsArk = {
        life.addNoahsArk()
        life.display()
    }
    val addCrab = {
        life.addCrab()
        life.display()
    }
    val frame = ControlsFrame(kill, addBlock, addDeadSparkCoil, addGlider, addGliderGun, addCopperhead, addNoahsArk, addCrab)
    frame.isVisible = true
}