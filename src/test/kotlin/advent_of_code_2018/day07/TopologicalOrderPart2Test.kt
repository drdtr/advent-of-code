package advent_of_code_2018.day07

import advent_of_code_2018.day07.TopologicalOrderPart2Test.ArgumentsProviders.ArgumentsProviderProcessingTime
import advent_of_code_2018.day07.TopologicalOrderUtil.Edge
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class TopologicalOrderPart2Test {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderProcessingTime::class)
    fun `test getProcessingTimeForAllNodesWithDependencies`(
        expected: Int,
        edges: List<Edge>,
        numOfWorkers: Int,
        processingTime: (Char) -> Int,
    ) = with(TopologicalOrderProcessingTime()) {
        assertEquals(expected, getProcessingTimeForAllNodesWithDependencies(edges, numOfWorkers, processingTime))
    }

    private object ArgumentsProviders {
        val edges1 = listOf(
            Edge('C', 'A'),
            Edge('C', 'F'),
            Edge('A', 'B'),
            Edge('A', 'D'),
            Edge('B', 'E'),
            Edge('D', 'E'),
            Edge('F', 'E'),
        )

        class ArgumentsProviderProcessingTime : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(15, edges1, 2, { ch: Char -> ch - 'A' + 1 }),
            )
        }
    }
}