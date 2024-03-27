package advent_of_code_2017.day12

import advent_of_code_2017.day12.ReachableNodes.*
import advent_of_code_2017.day12.ReachableNodesTest.ArgumentsProviderReachableNodes.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class ReachableNodesTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCountReachableNodes::class)
    fun `test countReachableNodes`(expected: Int, startNode: Int, graph: Graph) = with(ReachableNodes()) {
        assertEquals(expected, countReachableNodes(graph, startNode))
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCountConnectedComponents::class)
    fun `test countReachableNodes`(expected: Int, graph: Graph) = with(ReachableNodes()) {
        assertEquals(expected, countConnectedComponents(graph))
    }

    private interface ArgumentsProviderReachableNodes : ArgumentsProvider {
        companion object {
            val graph1 = mapOf(
                0 to listOf(2),
                1 to listOf(1),
                2 to listOf(0, 3, 4),
                3 to listOf(2, 4),
                4 to listOf(2, 3, 6),
                5 to listOf(6),
                6 to listOf(4, 5),
            )
        }

        class ArgumentsProviderCountReachableNodes : ArgumentsProviderReachableNodes {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> {
                return Stream.of(
                    Arguments.of(6, 0, graph1),
                    Arguments.of(1, 1, graph1),
                    Arguments.of(6, 2, graph1),
                    Arguments.of(6, 3, graph1),
                    Arguments.of(6, 4, graph1),
                    Arguments.of(6, 5, graph1),
                    Arguments.of(6, 6, graph1),
                )
            }
        }

        class ArgumentsProviderCountConnectedComponents : ArgumentsProviderReachableNodes {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> {
                return Stream.of(
                    Arguments.of(2, graph1),
                )
            }
        }
    }
}