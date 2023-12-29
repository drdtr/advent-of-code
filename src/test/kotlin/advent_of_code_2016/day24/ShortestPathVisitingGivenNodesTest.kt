package advent_of_code_2016.day24

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class ShortestPathVisitingGivenNodesTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderShortestPathVisitingGivenNodes::class)
    fun `test minLengthOfPathVisitingAllGivenNodes`(expected: Int, grid: List<String>) =
        with(ShortestPathVisitingGivenNodes()) {
            assertEquals(expected, minLengthOfPathVisitingAllGivenNodes(grid.map { it.toCharArray() }.toTypedArray()))
        }

    private class ArgumentsProviderShortestPathVisitingGivenNodes : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            arguments(
                14, listOf(
                    "###########",
                    "#0.1.....2#",
                    "#.#######.#",
                    "#4.......3#",
                    "###########",
                )
            )
        )
    }
}