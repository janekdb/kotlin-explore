@file:JvmName("Main")

package life

import java.awt.*
import kotlin.concurrent.fixedRateTimer

fun main() {
    EventQueue.invokeLater(::createAndShowUI)
}

private const val TITLE = "Kotlin life.Life"
private const val CELLS_PER_SIDE = 150

private fun createAndShowUI() {
    val frame = LifeFrame(TITLE, CELLS_PER_SIDE)
    frame.isVisible = true

    val life = Life(frame, CELLS_PER_SIDE)
    life.display()
    life.addGlider()
    life.randomize()
    life.display()

    fixedRateTimer(
        name = "generation-timer",
        initialDelay = 0, period = 100
    ) {
        life.step()
        life.display()
    }

}
