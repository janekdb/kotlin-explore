package life.obs

import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark

@OptIn(ExperimentalTime::class)
class Analytics {

    /* Circular buffer */
    private val MAX_DURATIONS = 100
    private val durations = Array<Duration>(MAX_DURATIONS) { 0.nanoseconds }
    private var durationsSize = 0
    private var durationsNext = 0

    fun recordEventDuration(duration: Duration): Unit {
        durations[durationsNext] = duration
        durationsSize = max(durationsSize, durationsNext + 1)
        durationsNext = (durationsNext + 1) % MAX_DURATIONS
    }

    fun averageEventDuration(): Duration {
        val totalNanos = durations.take(durationsSize).map { it.toLong(DurationUnit.NANOSECONDS) }.sumOf { it }

        val averageNanos = totalNanos / durationsSize
        return averageNanos.nanoseconds
    }

    private val MAX_START_TIMES = 100
    private val startTimes = Array<TimeMark?>(MAX_START_TIMES) { null }
    private var startTimesNext = 0
    fun recordEventStartTime(startTime: TimeMark) {
        startTimes.set(startTimesNext, startTime)
        startTimesNext++
    }

    /**
     * The duration of activity is taken as the duration between the
     * first event and the last event.
     * The count of events is one less than total number of recorded events.
     * An exception is thrown if less than two events have been recorded
     */
    fun eventsPerSecond(): Double {
        val numberOfEvents = startTimesNext - 1
        if (numberOfEvents < 1)
            throw RuntimeException("Not at least two events recorded")
        val windowStart = startTimes[0]
        require(windowStart != null)
        val windowEnd = startTimes[startTimesNext - 1]
        require(windowEnd != null)
        val intervalSec = (windowStart.elapsedNow() - windowEnd.elapsedNow()).toDouble(DurationUnit.SECONDS)
        return numberOfEvents / intervalSec
    }
}