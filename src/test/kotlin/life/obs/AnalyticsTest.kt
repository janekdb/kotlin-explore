package life.obs

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.microseconds

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
    fun forgetsHistory() {

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
}