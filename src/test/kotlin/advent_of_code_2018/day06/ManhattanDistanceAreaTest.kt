package advent_of_code_2018.day06

import advent_of_code_2018.day06.ManhattanDistanceArea.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class ManhattanDistanceAreaTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderManhattanDistanceArea::class)
    fun `test calcLargestFiniteArea`(expected: Int, points: List<Point>) =
        with(ManhattanDistanceArea()) {
            assertEquals(expected, calcLargestFiniteArea(points))
        }

    private class ArgumentsProviderManhattanDistanceArea : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                17, listOf(
                    Point(1, 1),
                    Point(1, 6),
                    Point(8, 3),
                    Point(3, 4),
                    Point(5, 5),
                    Point(8, 9),
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderManhattanDistanceAreaMaxDistSum::class)
    fun `test calcLargestAreaNearAllPointsBruteForce`(expected: Int, maxDistSum: Int, points: List<Point>) =
        with(ManhattanDistanceArea()) {
            assertEquals(expected, calcLargestAreaNearAllPointsBruteForce(maxDistSum, points))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderManhattanDistanceAreaMaxDistSum::class)
    fun `test calcLargestAreaNearAllPoints`(expected: Int, maxDistSum: Int, points: List<Point>) =
        with(ManhattanDistanceArea()) {
            assertEquals(expected, calcLargestAreaNearAllPoints(maxDistSum, points))
        }

    private class ArgumentsProviderManhattanDistanceAreaMaxDistSum : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                16, 32, listOf(
                    Point(1, 1),
                    Point(1, 6),
                    Point(8, 3),
                    Point(3, 4),
                    Point(5, 5),
                    Point(8, 9),
                )
            ),
            arguments(
                151, 100, listOf(
                    Point(1, 1),
                    Point(1, 6),
                    Point(3, 10),
                    Point(7, 3),
                    Point(3, 4),
                    Point(5, 5),
                    Point(8, 9),
                    Point(10, 16),
                )
            ),
            arguments(
                157, 120, listOf(
                    Point(1, 1),
                    Point(1, 10),
                    Point(1, 6),
                    Point(3, 10),
                    Point(7, 3),
                    Point(3, 4),
                    Point(5, 5),
                    Point(8, 9),
                    Point(10, 16),
                )
            ),
        )
    }
}
