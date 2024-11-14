package life.obs

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@OptIn(ExperimentalTime::class)
internal class AnalyticsTest {

    @Test
    fun reportsAverageEventDuration() {
        with(Analytics()) {
            recordEventDuration(1.microseconds)
            assertEquals(1.microseconds, averageEventDuration())

            recordEventDuration(3.microseconds)
            assertEquals(2.microseconds, averageEventDuration())

            recordEventDuration(2.microseconds)
            assertEquals(2.microseconds, averageEventDuration())
        }
    }

    @Test
    fun forgetsEventDurationHistory() {

        with(Analytics()) {
            repeat(100) {
                recordEventDuration(10.microseconds)
            }
            assertEquals(10.microseconds, averageEventDuration())
            recordEventDuration((99 * 100 - 99 * 10).microseconds)
            /*
             * (99 * 100 - 99 * 10) + 99 * 10 = 9900
             * 9900 / 100 = 99
             */
            assertEquals(99.microseconds, averageEventDuration())
        }
    }

    @Test
    fun reportEventRate() {
        val timeSource = TimeSource.Monotonic
        val mark1: TimeMark = timeSource.markNow()
        val mark2: TimeMark = mark1 + 5.seconds
        val mark3: TimeMark = mark2 + 1.seconds

        with(Analytics()) {
            recordEventStartTime(mark1)
            val exception = assertThrows<RuntimeException> { eventsPerSecond() }
            assert(exception.message?.contains("two events") == true)
            recordEventStartTime(mark2)
            run {
                val actualEventsPerSec = eventsPerSecond()
                val expectedEventsPerSec = 1.0 / 5
                assertEquals(expectedEventsPerSec, actualEventsPerSec, 0.01)
            }
            recordEventStartTime(mark3)
            run {
                val actualEventsPerSec = eventsPerSecond()
                val expectedEventsPerSec = 2.0 / 6
                assertEquals(expectedEventsPerSec, actualEventsPerSec, 0.01)
            }

        }
    }
}