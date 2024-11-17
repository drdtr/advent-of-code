package advent_of_code_2017.day22

import advent_of_code_2017.day22.GridNodeMarkerPart2.*
import advent_of_code_2017.day22.GridNodeMarkerPart2.NodeState.*
import advent_of_code_2017.day22.MarkedNodeUtil.nodeMarkingStringsToBooleanArrays
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class GridNodeMarkerPart2Test {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGridNodeMarker::class)
    fun `test markAndCount`(numOfMoves: Int, grid: List<String>, expected: MarkingOutcome) =
        with(GridNodeMarkerPart2()) {
            assertEquals(expected, markAndCount(nodeMarkingStringsToBooleanArrays(grid), numOfMoves))
        }

    private class ArgumentsProviderGridNodeMarker : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            arguments(
                0,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Marked,
                        Point(2, 1) to Marked,
                    ),
                    markedCount = 0
                )
            ),
            arguments(
                1,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Marked,
                        Point(1, 0) to Premarked,
                        Point(2, 1) to Marked,
                    ),
                    markedCount = 0
                )
            ),
            arguments(
                2,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Flagged,
                        Point(1, 0) to Premarked,
                        Point(2, 1) to Marked,
                    ),
                    markedCount = 0
                )
            ),
            arguments(
                5,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Premarked,
                        Point(0, 1) to Premarked,
                        Point(1, 0) to Flagged,
                        Point(1, 1) to Premarked,
                        Point(2, 0) to Premarked,
                        Point(3, 1) to Marked,
                    ),
                    markedCount = 0
                )
            ),
            arguments(
                6,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Premarked,
                        Point(0, 1) to Premarked,
                        Point(1, 1) to Premarked,
                        Point(2, 0) to Premarked,
                        Point(3, 1) to Marked,
                    ),
                    markedCount = 0
                )
            ),
            arguments(
                7,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Marked,
                        Point(0, 1) to Premarked,
                        Point(1, 1) to Premarked,
                        Point(2, 0) to Premarked,
                        Point(3, 1) to Marked,
                    ),
                    markedCount = 1
                )
            ),
            arguments(
                8,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    nodeStates = mapOf(
                        Point(0, 0) to Premarked,
                        Point(1, 0) to Marked,
                        Point(1, 1) to Premarked,
                        Point(2, 1) to Premarked,
                        Point(3, 0) to Premarked,
                        Point(4, 1) to Marked,
                    ),
                    markedCount = 1
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGridNodeMarkerOnlyCount::class)
    fun `test markAndCount - only count`(numOfMoves: Int, grid: List<String>, expected: Int) =
        with(GridNodeMarkerPart2()) {
            assertEquals(expected, markAndCount(nodeMarkingStringsToBooleanArrays(grid), numOfMoves).markedCount)
        }

    private class ArgumentsProviderGridNodeMarkerOnlyCount : ArgumentsProvider {
        private val grid0 = listOf(
            "..#",
            "#..",
            "...",
        )

        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            arguments(0, grid0, 0),
            arguments(1, grid0, 0),
            arguments(2, grid0, 0),
            arguments(5, grid0, 0),
            arguments(6, grid0, 0),
            arguments(7, grid0, 1),
            arguments(8, grid0, 1),
            arguments(100, grid0, 26),
            arguments(10000000, grid0, 2511944),
        )
    }
}