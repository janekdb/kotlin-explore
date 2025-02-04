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
    private var startTimeSize = -1
    private var startTimesNext = 0
    private var startTimesLast = -1
    fun recordEventStartTime(startTime: TimeMark) {
        startTimes[startTimesNext] = startTime
        startTimesLast = startTimesNext
        startTimeSize = max(startTimeSize, startTimesNext + 1)
        startTimesNext = (startTimesNext + 1) % MAX_START_TIMES
    }

    /**
     * The duration of activity is taken as the duration between the
     * first event and the last event.
     * The count of events is one less than total number of recorded events.
     * An exception is thrown if fewer than two events have been recorded
     */
    fun eventsPerSecond(): Double {
        val numberOfEvents = startTimeSize
        if (numberOfEvents < 2)
            throw RuntimeException("Not at least two events recorded")
        val windowStartIndex =
            if (startTimeSize < MAX_START_TIMES) 0
            else (startTimesLast + 1) % MAX_START_TIMES

        val windowStart = startTimes[windowStartIndex]
        require(windowStart != null)
        val windowEnd = startTimes[startTimesLast]
        require(windowEnd != null)
        val intervalSec = (windowStart.elapsedNow() - windowEnd.elapsedNow()).toDouble(DurationUnit.SECONDS)
        val numberOfIntervals = numberOfEvents - 1
        return numberOfIntervals / intervalSec
    }
}
/*
Rules for a circular buffer.

Take the array to be size 5
- = no value recorded
N = value recorded
n = value recorded
first = index of earliest value
last = index of latest value
size = array utilization

----- ; first = -, last = -, size = 0
N---- ; first = 0, last = 0, size = 1
NN--- ; first = 0, last = 1, size = 2
NNN-- ; first = 0, last = 2, size = 3
NNNN- ; first = 0, last = 3, size = 4
NNNNN ; first = 0, last = 4, size = 5
nNNNN ; first = 1, last = 0, size = 5
nnNNN ; first = 2, last = 1, size = 5
nnnNN ; first = 3, last = 2, size = 5
nnnnN ; first = 4, last = 3, size = 5
nnnnn ; first = 0, last = 4, size = 5
Nnnnn ; first = 1, last = 0, size = 5

Rules-
- If size < MAX, first = 0
- If size = MAX, first = (last + 1) % MAX
 */