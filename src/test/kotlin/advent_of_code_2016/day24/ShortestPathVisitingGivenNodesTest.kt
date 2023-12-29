package advent_of_code_2016.day24

import advent_of_code_2016.day24.ShortestPathVisitingGivenNodesTest.ArgumentsProviderShortestPathVisitingGivenNodes.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class ShortestPathVisitingGivenNodesTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMinLengthOfPathVisitingAllGivenNodes::class)
    fun `test minLengthOfPathVisitingAllGivenNodes`(expected: Int, grid: List<String>) =
        with(ShortestPathVisitingGivenNodes()) {
            val gridMapped = grid.map { it.toCharArray() }.toTypedArray()
            assertEquals(expected, minLengthOfPathVisitingAllGivenNodes(gridMapped, returnToStart = false))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMinLengthOfPathVisitingAllGivenNodesAndReturningToStart::class)
    fun `test minLengthOfPathVisitingAllGivenNodes, returnToStart=true`(expected: Int, grid: List<String>) =
        with(ShortestPathVisitingGivenNodes()) {
            val gridMapped = grid.map { it.toCharArray() }.toTypedArray()
            assertEquals(expected, minLengthOfPathVisitingAllGivenNodes(gridMapped, returnToStart = true))
        }

    private interface ArgumentsProviderShortestPathVisitingGivenNodes : ArgumentsProvider {
        companion object {
            private val grid5x11 = listOf(
                "###########",
                "#0.1.....2#",
                "#.#######.#",
                "#4.......3#",
                "###########",
            )
        }

        class ArgumentsProviderMinLengthOfPathVisitingAllGivenNodes : ArgumentsProviderShortestPathVisitingGivenNodes {
            override fun provideArguments(context: ExtensionContext?) =
                Stream.of(arguments(14, grid5x11))
        }

        class ArgumentsProviderMinLengthOfPathVisitingAllGivenNodesAndReturningToStart :
            ArgumentsProviderShortestPathVisitingGivenNodes {
            override fun provideArguments(context: ExtensionContext?) =
                Stream.of(arguments(20, grid5x11))
        }
    }
}