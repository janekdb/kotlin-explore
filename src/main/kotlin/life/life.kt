@file:JvmName("Main")

package life

import life.control.ControlsFrame
import life.obs.Analytics
import java.awt.*
import kotlin.concurrent.fixedRateTimer
import kotlin.system.measureNanoTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlin.time.Duration.Companion.nanoseconds

fun main() {
    println("Kotlin version: ${KotlinVersion.CURRENT}")
    EventQueue.invokeLater(::createAndShowUI)
}

private const val TITLE = "Kotlin Life"
private const val CELLS_PER_SIDE = 150

@OptIn(ExperimentalTime::class)
private fun createAndShowUI() {

    val life = createLife()

    life.display()
    life.randomize()
    life.display()

    createControls(life)

    val stepLogger = EventDurationAnalyticsLogger("step")
    val stepAction = Action(stepLogger) { life.step() }

    val displayLogger = EventDurationAnalyticsLogger("display")
    val displayAction = Action(displayLogger) { life.display() }

    val startTimeLogger = EventStartTimeAnalyticsLogger("update")

    val timeSource = TimeSource.Monotonic

    val limiter = Limiter(10)

    fixedRateTimer(
        name = "generation-timer",
        initialDelay = 0, period = 25
    ) {
        startTimeLogger.record(timeSource.markNow())
        val display = limiter.get()
        stepAction.doAction(display)
        displayAction.doAction(display)
        if (display)
            startTimeLogger.display()
    }

}

/**
 * Helper class to call an action and display the execution time analytics
 */
private class Action(
    private val analyticsLogger: EventDurationAnalyticsLogger,
    private val action: () -> Unit
) {
    fun doAction(display: Boolean): Unit {
        val actionNanos = measureNanoTime { action() }
        analyticsLogger.record(actionNanos.nanoseconds)
        if (display)
            analyticsLogger.display()
    }
}

/**
 * Limit the rate at which an action is taken
 * @param delaySecs The gap to ensure between returning true from [get]
 */
private class Limiter(delaySecs: Int) {
    private val gapNanos = delaySecs.toLong() * 1_000_000_000
    private var nextNanos = System.nanoTime() + gapNanos

    /** @return true no earlier than `delaySecs` after tha previous
     * time true was returned
     */
    fun get(): Boolean {
        val result = System.nanoTime() >= nextNanos
        if (result)
            nextNanos = System.nanoTime() + gapNanos
        return result
    }
}

/**
 * Helper class to encapsulate collecting and display of execution times
 */
private class EventDurationAnalyticsLogger(private val label: String) {
    private val analytics = Analytics()

    fun record(duration: Duration): Unit {
        analytics.recordEventDuration(duration)
    }

    fun display(): Unit {
        val average = analytics.averageEventDuration()
        println("Average duration for $label: $average")
    }
}

/**
 * Helper class to encapsulate collecting and displaying of event start times
 */
@OptIn(ExperimentalTime::class)
private class EventStartTimeAnalyticsLogger(private val label: String) {
    private val analytics = Analytics()
    private var recordedEvents = 0

    fun record(startTime: TimeMark) {
        analytics.recordEventStartTime(startTime)
        /* Avoid wraparound */
        if (recordedEvents < 2)
            recordedEvents++
    }

    fun display() {
        /* Avoid exception when fewer than 2 events */
        if (recordedEvents < 2)
            return
        val eventsPerSec = analytics.eventsPerSecond()
        println("Events/sec for $label: $eventsPerSec")
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