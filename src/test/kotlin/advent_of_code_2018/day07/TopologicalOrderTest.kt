package advent_of_code_2018.day07

import advent_of_code_2018.day07.TopologicalOrder.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class TopologicalOrderTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderTopologicalOrder::class)
    fun `test getTopologicalOrderOfNodes`(expected: String, edges: List<Edge>) = with(TopologicalOrder()) {
        assertEquals(expected, getTopologicalOrderOfNodes(edges))
    }

    private class ArgumentsProviderTopologicalOrder : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                "CABDFE", listOf(
                    Edge('C', 'A'),
                    Edge('C', 'F'),
                    Edge('A', 'B'),
                    Edge('A', 'D'),
                    Edge('B', 'E'),
                    Edge('D', 'E'),
                    Edge('F', 'E'),
                )
            ),
        )
    }

}