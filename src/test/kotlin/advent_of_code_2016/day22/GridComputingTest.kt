package advent_of_code_2016.day22

import advent_of_code_2016.day22.GridComputing.*
import advent_of_code_2016.day22.GridComputingTest.ArgumentsProviderGridComputing.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class GridComputingTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGridComputingViablePairs::class)
    fun `test countViablePairs`(expected: Int, nodes: List<Node>) = with(GridComputing()) {
        assertEquals(expected, countViablePairs(nodes))
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGridComputingViablePairs::class)
    fun `test minStepsToMoveDataFromSrcToDest`(
        expected: Int,
        srcPoint: Pair<Int, Int>,
        destPoint: Pair<Int, Int>,
        nodes: List<Node>,
    ) = with(GridComputing()) {
        // TODO
    }

    private interface ArgumentsProviderGridComputing : ArgumentsProvider {
        companion object {
            private val grid3x3 = listOf(
                Node(y = 0, x = 0, used = 8, avail = 10),
                Node(y = 0, x = 1, used = 7, avail = 9),
                Node(y = 0, x = 2, used = 6, avail = 10),
                Node(y = 1, x = 0, used = 6, avail = 11),
                Node(y = 1, x = 1, used = 0, avail = 8),
                Node(y = 1, x = 2, used = 8, avail = 9),
                Node(y = 2, x = 0, used = 28, avail = 32),
                Node(y = 2, x = 1, used = 7, avail = 11),
                Node(y = 2, x = 2, used = 6, avail = 9),
            )
        }

        class ArgumentsProviderGridComputingViablePairs : ArgumentsProviderGridComputing {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> {
                return Stream.of(Arguments.of(56, grid3x3))
            }
        }

        class ArgumentsProviderGridComputingMinSteps : ArgumentsProviderGridComputing {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> {
                return Stream.of(Arguments.of(7, 0 to 2, 0 to 0, grid3x3))
            }
        }
    }
}