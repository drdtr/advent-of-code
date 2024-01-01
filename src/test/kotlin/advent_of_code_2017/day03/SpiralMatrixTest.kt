package advent_of_code_2017.day03

import advent_of_code_2017.day03.SpiralMatrix.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class SpiralMatrixTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSpiralMatrixCellCoordinates::class)
    fun `test calcCellCoordinates`(expected: Point, n: Int) = with(SpiralMatrix()) {
        assertEquals(expected, calcCellCoordinates(n))
    }

    private class ArgumentsProviderSpiralMatrixCellCoordinates : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(Point(0, 0), 1),
            Arguments.of(Point(1, 0), 2),
            Arguments.of(Point(1, 1), 3),
            Arguments.of(Point(0, 1), 4),
            Arguments.of(Point(-1, 1), 5),
            Arguments.of(Point(-1, 0), 6),
            Arguments.of(Point(-1, -1), 7),
            Arguments.of(Point(0, -1), 8),
            Arguments.of(Point(1, -1), 9),
            Arguments.of(Point(2, -1), 10),
            Arguments.of(Point(2, 0), 11),
            Arguments.of(Point(2, 1), 12),
            Arguments.of(Point(2, 2), 13),
            Arguments.of(Point(1, 2), 14),
            Arguments.of(Point(0, 2), 15),
            Arguments.of(Point(-1, 2), 16),
            Arguments.of(Point(-2, 2), 17),
            Arguments.of(Point(-2, 1), 18),
            Arguments.of(Point(-2, 0), 19),
            Arguments.of(Point(-2, -1), 20),
            Arguments.of(Point(-2, -2), 21),
            Arguments.of(Point(-1, -2), 22),
            Arguments.of(Point(0, -2), 23),
            Arguments.of(Point(1, -2), 24),
            Arguments.of(Point(2, -2), 25),
        )
    }
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSpiralMatrix::class)
    fun `test calcManhattanDistance`(expected: Int, n: Int) = with(SpiralMatrix()) {
        assertEquals(expected, calcManhattanDistance(n))
    }

    private class ArgumentsProviderSpiralMatrix : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, 1),
            Arguments.of(3, 12),
            Arguments.of(2, 23),
            Arguments.of(31, 1024),
        )
    }
}