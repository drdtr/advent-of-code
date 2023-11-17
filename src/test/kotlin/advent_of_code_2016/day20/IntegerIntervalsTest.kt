package advent_of_code_2016.day20

import advent_of_code_2016.day20.IntegerIntervals.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class IntegerIntervalsTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderIntegerIntervals::class)
    fun `test minNotInAnyInterval`(expected: IntegerIntervalsResult, intervals: List<LongRange>) =
        with(IntegerIntervals()) {
            assertEquals(expected, findMinNotInIntervalAndTotalNumCount(intervals))
        }

    private class ArgumentsProviderIntegerIntervals : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                IntegerIntervalsResult(minNotInAnyInterval = 0, totalNumCountInAllIntervals = 3),
                listOf(2..4L)
            ),
            Arguments.of(
                IntegerIntervalsResult(minNotInAnyInterval = 3, totalNumCountInAllIntervals = 8),
                listOf(
                    5..8L,
                    0..2L,
                    4..7L,
                )
            ),
            Arguments.of(
                IntegerIntervalsResult(minNotInAnyInterval = 9, totalNumCountInAllIntervals = 20),
                listOf(
                    5..8L,
                    4..7L,
                    10..20L,
                    0..2L,
                    1..3L,
                )
            ),
            Arguments.of(
                IntegerIntervalsResult(minNotInAnyInterval = 21, totalNumCountInAllIntervals = 21),
                listOf(
                    5..8L,
                    4..7L,
                    9..20L,
                    11..15L,
                    0..2L,
                    1..3L,
                )
            ),
        )
    }
}