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
}
