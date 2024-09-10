@file:JvmName("Main")

package life

import life.control.ControlsFrame
import life.obs.Analytics
import java.awt.*
import kotlin.concurrent.fixedRateTimer
import kotlin.system.measureNanoTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

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

    val stepLogger = AnalyticsLogger("step")
    val stepAction = Action(stepLogger) { life.step() }

    val displayLogger = AnalyticsLogger("display")
    val displayAction = Action(displayLogger) { life.display() }

    fixedRateTimer(
        name = "generation-timer",
        initialDelay = 0, period = 50
    ) {
        stepAction.doAction()
        displayAction.doAction()
    }

}

/**
 * Helper class to call an action and display the execution time analytics
 */
private class Action(
    private val analyticsLogger: AnalyticsLogger,
    private val action: () -> Unit
) {
    fun doAction(): Unit {
        val actionNanos = measureNanoTime { action() }
        analyticsLogger.record(actionNanos.nanoseconds)
        analyticsLogger.display()
    }
}

/**
 * Helper class to encapsulate collecting and logging of execution times
 */
private class AnalyticsLogger(private val label: String) {
    private val analytics = Analytics()
    private val displayGapNanos = 10_000_000_000
    private var nextDisplayNanos = System.nanoTime() + displayGapNanos

    fun record(duration: Duration): Unit {
        analytics.recordEventDuration(duration)
    }

    fun display(): Unit {
        if (System.nanoTime() < nextDisplayNanos)
            return
        val average = analytics.averageEventDuration()
        println("Average duration for $label: $average")
        nextDisplayNanos = System.nanoTime() + displayGapNanos
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
    val frame =
        ControlsFrame(kill, addBlock, addDeadSparkCoil, addGlider, addGliderGun, addCopperhead, addNoahsArk, addCrab)
    frame.isVisible = true
}