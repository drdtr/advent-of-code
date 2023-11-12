package advent_of_code_2016.day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class SafeAndTrapTilesTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderStatefulMazeShortestPath::class)
    fun `test shortestPath`(expected: Int, n: Int, startingRow: String) = with(SafeAndTrapTiles()) {
        assertEquals(expected, countSafeTiles(n, startingRow))
    }

    private class ArgumentsProviderStatefulMazeShortestPath : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(6, 3, "..^^."),
            Arguments.of(38, 10, ".^^.^.^^^^"),
        )
    }
}