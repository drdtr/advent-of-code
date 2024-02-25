package advent_of_code_2017.day11

import advent_of_code_2017.day11.HexGridDistance.*
import advent_of_code_2017.day11.HexGridDistance.HexGridStep.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class HexGridDistanceTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderHexGridDistance::class)
    fun `test calcStepDistance`(expected: Int, steps: List<HexGridStep>) = with(HexGridDistance()) {
        assertEquals(expected, calcStepDistance(steps))
    }

    private class ArgumentsProviderHexGridDistance : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(3, listOf(UpRight, UpRight, UpRight)),
            Arguments.of(0, listOf(UpRight, UpRight, DownLeft, DownLeft)),
            Arguments.of(2, listOf(UpRight, UpRight, Down, Down)),
            Arguments.of(3, listOf(DownRight, DownLeft, DownRight, DownLeft, DownLeft)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMaxHexGridDistanceReached::class)
    fun `test calcMaxStepDistanceReached`(expected: Int, steps: List<HexGridStep>) = with(HexGridDistance()) {
        assertEquals(expected, calcMaxStepDistanceReached(steps))
    }

    private class ArgumentsProviderMaxHexGridDistanceReached : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(3, listOf(UpRight, UpRight, UpRight)),
            Arguments.of(2, listOf(UpRight, UpRight, DownLeft, DownLeft)),
            Arguments.of(2, listOf(UpRight, UpRight, Down, Down)),
            Arguments.of(3, listOf(DownRight, DownLeft, DownRight, DownLeft, DownLeft)),
        )
    }
}