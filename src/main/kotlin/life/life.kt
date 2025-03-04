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

    fixedRateTimer(
        name = "generation-timer",
        initialDelay = 0, period = 25
    ) {
        startTimeLogger.record(timeSource.markNow())
        stepAction.doAction()
        displayAction.doAction()
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
    fun doAction(): Unit {
        val actionNanos = measureNanoTime { action() }
        analyticsLogger.record(actionNanos.nanoseconds)
        analyticsLogger.display()
    }
}

/**
 * Helper class to encapsulate collecting and display of execution times
 */
private class EventDurationAnalyticsLogger(private val label: String) {
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

/**
 * Helper class to encapsulate collecting and displaying of event start times
 */
@OptIn(ExperimentalTime::class)
private class EventStartTimeAnalyticsLogger(private val label: String) {
    private val analytics = Analytics()
    private val displayGapNanos = 10_000_000_000
    private var nextDisplayNanos = System.nanoTime() + displayGapNanos
    private var recordedEvents = 0

    fun record(startTime: TimeMark) {
        analytics.recordEventStartTime(startTime)
        /* Avoid wraparound */
        if (recordedEvents < 2)
            recordedEvents++
    }

    fun display() {
        if (System.nanoTime() < nextDisplayNanos)
            return
        /* Avoid exception when less than 2 events */
        if (recordedEvents < 2)
            return
        val eventsPerSec = analytics.eventsPerSecond()
        println("Events/sec for $label: $eventsPerSec")
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