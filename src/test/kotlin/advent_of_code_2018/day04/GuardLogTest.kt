package advent_of_code_2018.day04

import advent_of_code_2018.day04.GuardLog.*
import advent_of_code_2018.day04.GuardLog.GuardLogEntry.*
import advent_of_code_2018.day04.GuardLogTest.ArgumentsProviderGuardLog.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDateTime
import java.util.stream.Stream

class GuardLogTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderFindMostAsleepGuard::class)
    fun `test findMostAsleepGuard`(expected: MostAsleepGuard, logEntries: List<GuardLogEntry>) =
        with(GuardLog()) {
            assertEquals(expected, findMostAsleepGuard(logEntries))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderFindMostAsleepGuardOnSameMinute::class)
    fun `test findMostAsleepGuardOnSameMinute`(expected: MostAsleepGuard, logEntries: List<GuardLogEntry>) =
        with(GuardLog()) {
            assertEquals(expected, findMostAsleepGuardOnSameMinute(logEntries))
        }

    private interface ArgumentsProviderGuardLog : ArgumentsProvider {
        companion object {
            val logEntries1 = listOf(
                GuardBeginsShiftEntry(LocalDateTime.of(1518, 11, 1, 0, 0), 10),
                GuardFallsAsleepEntry(LocalDateTime.of(1518, 11, 1, 0, 5)),
                GuardWakesUpEntry(LocalDateTime.of(1518, 11, 1, 0, 25)),
                GuardFallsAsleepEntry(LocalDateTime.of(1518, 11, 1, 0, 30)),
                GuardWakesUpEntry(LocalDateTime.of(1518, 11, 1, 0, 55)),
                GuardBeginsShiftEntry(LocalDateTime.of(1518, 11, 1, 23, 58), 99),
                GuardFallsAsleepEntry(LocalDateTime.of(1518, 11, 2, 0, 40)),
                GuardWakesUpEntry(LocalDateTime.of(1518, 11, 2, 0, 50)),
                GuardBeginsShiftEntry(LocalDateTime.of(1518, 11, 3, 0, 5), 10),
                GuardFallsAsleepEntry(LocalDateTime.of(1518, 11, 3, 0, 24)),
                GuardWakesUpEntry(LocalDateTime.of(1518, 11, 3, 0, 29)),
                GuardBeginsShiftEntry(LocalDateTime.of(1518, 11, 4, 0, 2), 99),
                GuardFallsAsleepEntry(LocalDateTime.of(1518, 11, 4, 0, 36)),
                GuardWakesUpEntry(LocalDateTime.of(1518, 11, 4, 0, 46)),
                GuardBeginsShiftEntry(LocalDateTime.of(1518, 11, 5, 0, 3), 99),
                GuardFallsAsleepEntry(LocalDateTime.of(1518, 11, 5, 0, 45)),
                GuardWakesUpEntry(LocalDateTime.of(1518, 11, 5, 0, 55)),
            )
        }

        class ArgumentsProviderFindMostAsleepGuard : ArgumentsProviderGuardLog {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(MostAsleepGuard(10, 24), logEntries1)
            )
        }

        class ArgumentsProviderFindMostAsleepGuardOnSameMinute : ArgumentsProviderGuardLog {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(MostAsleepGuard(99, 45), logEntries1)
            )
        }
    }
}
