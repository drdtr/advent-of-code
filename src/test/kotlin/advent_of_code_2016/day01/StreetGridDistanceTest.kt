package advent_of_code_2016.day01

import advent_of_code_2015.day02.PaperAndRibbonForPresent.*
import advent_of_code_2015.day23.ProcessorSimulator.*
import advent_of_code_2015.day23.ProcessorSimulator.Registers.*
import advent_of_code_2016.day01.StreetGridDistance.*
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.runners.Parameterized.*
import java.util.stream.Stream

internal class StreetGridDistanceTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderShortestDistanceToDestination::class)
    fun `test shortestDistanceToDestination`(expected: Int, steps: List<Step>) = with(StreetGridDistance()) {
        assertEquals(expected, shortestDistanceToDestination(steps))
    }

    private class ArgumentsProviderShortestDistanceToDestination : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(5, listOf(Right(2), Left(3))),
            Arguments.of(2, listOf(Left(2), Left(2), Left(2))),
            Arguments.of(2, listOf(Right(2), Right(2), Right(2))),
            Arguments.of(12, listOf(Right(5), Left(5), Right(5), Right(3))),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderShortestDistanceToFirstPointVisitedTwice::class)
    fun `test shortestDistanceToFirstPointVisitedTwice`(expected: Int, steps: List<Step>) = with(StreetGridDistance()) {
        assertEquals(expected, shortestDistanceToFirstPointVisitedTwice(steps))
    }

    private class ArgumentsProviderShortestDistanceToFirstPointVisitedTwice : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(4, listOf(Right(8), Right(4), Right(4), Right(8))),
        )
    }
}