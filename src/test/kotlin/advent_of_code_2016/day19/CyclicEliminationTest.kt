package advent_of_code_2016.day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class CyclicEliminationTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCyclicElimination::class)
    fun `test lastUneliminated`(expected: Int, n: Int) = with(CyclicElimination()) {
        assertEquals(expected, lastUneliminated(n))
    }

    private class ArgumentsProviderCyclicElimination : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, 1),
            Arguments.of(0, 2),
            Arguments.of(2, 3),
            Arguments.of(0, 4),
            Arguments.of(2, 5),
            Arguments.of(4, 6),
            Arguments.of(6, 7),
            Arguments.of(0, 8),
            Arguments.of(2, 9),
        )
    }
}