package advent_of_code_2017.day22

import advent_of_code_2017.day22.GridNodeMarkerPart1.*
import advent_of_code_2017.day22.MarkedNodeUtil.nodeMarkingStringsToBooleanArrays
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class GridNodeMarkerPart1Test {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGridNodeMarker::class)
    fun `test markAndCount`(numOfMoves: Int, grid: List<String>, expected: MarkingOutcome) =
        with(GridNodeMarkerPart1()) {
            assertEquals(expected, markAndCount(nodeMarkingStringsToBooleanArrays(grid), numOfMoves))
        }

    private class ArgumentsProviderGridNodeMarker : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                0,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    markedNodes = setOf(
                        Point(0, 0),
                        Point(2, 1),
                    ),
                    markedCount = 0
                )
            ),
            Arguments.of(
                1,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    markedNodes = setOf(
                        Point(0, 0),
                        Point(1, 0),
                        Point(2, 1),
                    ),
                    markedCount = 1
                )
            ),
            Arguments.of(
                2,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    markedNodes = setOf(
                        Point(0, 0),
                        Point(1, 1),
                    ),
                    markedCount = 1
                )
            ),
            Arguments.of(
                6,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    markedNodes = setOf(
                        Point(0, 0),
                        Point(0, 1),
                        Point(1, 0),
                        Point(1, 1),
                        Point(2, 0),
                        Point(3, 1),
                    ),
                    markedCount = 5
                )
            ),
            Arguments.of(
                7,
                listOf(
                    "..#",
                    "#..",
                    "...",
                ),
                MarkingOutcome(
                    markedNodes = setOf(
                        Point(0, 0),
                        Point(0, 1),
                        Point(1, 0),
                        Point(2, 0),
                        Point(3, 1),
                    ),
                    markedCount = 5
                )
            ),
        )
    }
}