package advent_of_code_2017.day19

import advent_of_code_2017.day19.RouteWithLetters.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class RouteWithLettersTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderRouteWithLetters::class)
    fun `test traverseRouteAndCollectLetters`(expected: RouteTraversalResult, route: List<String>) =
        with(RouteWithLetters()) {
            assertEquals(expected, traverseRouteAndCollectLetters(route))
        }

    private class ArgumentsProviderRouteWithLetters : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            arguments(
                RouteTraversalResult(38, "ABCDEF"),
                listOf(
                    "     |         ",
                    "     |  +--+   ",
                    "     A  |  C   ",
                    " F---|----E|--+",
                    "     |  |  |  D",
                    "     +B-+  +--+",
                )
            ),
        )
    }
}
