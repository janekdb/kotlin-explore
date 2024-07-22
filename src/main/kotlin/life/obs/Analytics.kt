package life.obs

import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

class Analytics {

    /* Circular buffer */
    private val MAX_DURATIONS = 100
    private val durations = Array<Duration>(MAX_DURATIONS) { 0.nanoseconds }
    private var durationsSize = 0
    private var durationsNext = 0

    fun recordEventDuration(duration: Duration): Unit {
        durations.set(durationsNext, duration)
        durationsSize = max(durationsSize, durationsNext + 1)
        durationsNext = (durationsNext + 1) % MAX_DURATIONS
    }

    fun averageEventDuration(): Duration {
        val totalNanos = durations.take(durationsSize).map { it.toLong(DurationUnit.NANOSECONDS) }.sumOf { it }

        val averageNanos = totalNanos / durationsSize
        return averageNanos.nanoseconds
    }
}