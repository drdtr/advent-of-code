package advent_of_code_2016.day17

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class StatefulMazeTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderStatefulMazeShortestPath::class)
    fun `test shortestPath`(expected: String, passcode: String) = with(StatefulMaze()) {
        assertEquals(expected, shortestPath(passcode))
    }

    private class ArgumentsProviderStatefulMazeShortestPath : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("DDRRRD", "ihgpwlah"),
            Arguments.of("DDUDRLRRUDRD", "kglvqrro"),
            Arguments.of("DRURDRUDDLLDLUURRDULRLDUUDDDRR", "ulqzkmiv"),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderStatefulMazeMaxPathLength::class)
    fun `test maxPathLength`(expected: Int, passcode: String) = with(StatefulMaze()) {
        assertEquals(expected, maxPathLength(passcode))
    }

    private class ArgumentsProviderStatefulMazeMaxPathLength : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(370, "ihgpwlah"),
            Arguments.of(492, "kglvqrro"),
            Arguments.of(830, "ulqzkmiv"),
        )
    }
}