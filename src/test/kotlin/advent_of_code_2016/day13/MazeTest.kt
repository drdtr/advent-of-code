package advent_of_code_2016.day13

import advent_of_code_2016.day10.BotSimulatorTest.*
import advent_of_code_2016.day13.Maze.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class MazeTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMazeMinStepsToReach::class)
    fun `test minStepsToReach`(expected: Int?, k: Int, startingPoint: Point, targetPoint: Point) = with(Maze()) {
        assertEquals(expected, minStepsToReach(k, startingPoint, targetPoint))
    }

    private class ArgumentsProviderMazeMinStepsToReach : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(13, 10, Point(0, 0), Point(7, 4)),
            Arguments.of(11, 10, Point(1, 1), Point(7, 4)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMazeNumberOfReachableCells::class)
    fun `test numberOfReachableCells`(expected: Int, k: Int, startingPoint: Point, maxDist: Int) = with(Maze()) {
        assertEquals(expected, numberOfReachableCells(k, startingPoint, maxDist))
    }

    private class ArgumentsProviderMazeNumberOfReachableCells : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(4, 10, Point(0, 0), 3),
            Arguments.of(1, 10, Point(1, 1), 0),
            Arguments.of(3, 10, Point(1, 1), 1),
            Arguments.of(5, 10, Point(1, 1), 2),
            Arguments.of(6, 10, Point(1, 1), 3),
            Arguments.of(10, 10, Point(2, 2), 3),
        )
    }
}