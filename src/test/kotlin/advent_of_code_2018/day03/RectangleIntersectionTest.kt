package advent_of_code_2018.day03

import advent_of_code_2018.day03.RectangleIntersection.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class RectangleIntersectionTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderRectangleIntersection::class)
    fun `test calcIntersectionArea`(expected: Int, rectangles: List<Rectangle>) = with(RectangleIntersection()) {
        assertEquals(expected, calcIntersectionArea(rectangles))
    }

    private class ArgumentsProviderRectangleIntersection : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                0, listOf(
                    Rectangle(1, 3, 4, 4),
                )
            ),
            arguments(
                0, listOf(
                    Rectangle(1, 3, 4, 4),
                    Rectangle(5, 5, 2, 2),
                )
            ),
            arguments(
                0, listOf(
                    Rectangle(3, 1, 4, 4),
                    Rectangle(5, 5, 2, 2),
                )
            ),
            arguments(
                4, listOf(
                    Rectangle(1, 3, 4, 4),
                    Rectangle(3, 1, 4, 4),
                    Rectangle(5, 5, 2, 2),
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderRectangleIntersectionFindNonIntersecting::class)
    fun `test findNonIntersectingRectangles`(expected: List<Int>, rectangles: List<Rectangle>) =
        with(RectangleIntersection()) {
            assertEquals(expected, findNonIntersectingRectangles(rectangles).map { it.id })
        }

    private class ArgumentsProviderRectangleIntersectionFindNonIntersecting : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                listOf(1), listOf(
                    Rectangle(1, 3, 4, 4, id = 1),
                )
            ),
            arguments(
                listOf(1, 2), listOf(
                    Rectangle(1, 3, 4, 4, id = 1),
                    Rectangle(5, 5, 2, 2, id = 2),
                )
            ),
            arguments(
                listOf(1, 3), listOf(
                    Rectangle(3, 1, 4, 4, id = 1),
                    Rectangle(5, 5, 2, 2, id = 3),
                )
            ),
            arguments(
                listOf(3), listOf(
                    Rectangle(1, 3, 4, 4, id = 1),
                    Rectangle(3, 1, 4, 4, id = 2),
                    Rectangle(5, 5, 2, 2, id = 3),
                )
            ),
        )
    }
}