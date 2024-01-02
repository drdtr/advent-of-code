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

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSpiralMatrixCellCoordinates::class)
    fun `test SpiralCellCoordinatesIterator`(expected: Point, n: Int) = with(SpiralCellCoordinatesIterator()) {
        repeat(n - 1) { next() }
        assertEquals(expected, next())
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

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderFirstSumMatrixValueGreaterThanGiven::class)
    fun `test firstSumMatrixValueGreaterThan`(expected: Int, n: Int) = with(SpiralMatrix()) {
        assertEquals(expected, firstSumMatrixValueGreaterThan(n))
    }

    private class ArgumentsProviderFirstSumMatrixValueGreaterThanGiven : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(1, 0),
            Arguments.of(2, 1),
            Arguments.of(4, 2),
            Arguments.of(4, 3),
            Arguments.of(5, 4),
            Arguments.of(10, 5),
            Arguments.of(10, 6),
            Arguments.of(10, 9),
            Arguments.of(11, 10),
            Arguments.of(23, 11),
            Arguments.of(23, 12),
            Arguments.of(23, 22),
            Arguments.of(25, 23),
            Arguments.of(25, 24),
            Arguments.of(26, 25),
            Arguments.of(54, 26),
            Arguments.of(54, 53),
            Arguments.of(57, 54),
            Arguments.of(57, 56),
            Arguments.of(59, 57),
            Arguments.of(59, 58),
            Arguments.of(122, 59),
            Arguments.of(122, 60),
            Arguments.of(122, 121),
            Arguments.of(133, 122),
            Arguments.of(133, 123),
            Arguments.of(133, 132),
            Arguments.of(142, 133),
            Arguments.of(142, 134),
            Arguments.of(142, 141),
            Arguments.of(147, 142),
            Arguments.of(147, 143),
            Arguments.of(147, 146),
            Arguments.of(304, 147),
            Arguments.of(304, 148),
            Arguments.of(304, 303),
            Arguments.of(330, 304),
            Arguments.of(330, 305),
            Arguments.of(330, 329),
            Arguments.of(351, 330),
            Arguments.of(351, 331),
            Arguments.of(351, 350),
            Arguments.of(362, 351),
            Arguments.of(362, 352),
            Arguments.of(362, 361),
            Arguments.of(747, 362),
            Arguments.of(747, 363),
            Arguments.of(747, 746),
            Arguments.of(806, 747),
            Arguments.of(806, 748),
            Arguments.of(806, 805),
        )
    }
}