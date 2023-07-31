package advent_of_code_2016.day07

import advent_of_code_2016.day06.RepetitionCoding.CharacterRestoringMode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class CheckLetterPairsTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCheckLetterPairs::class)
    fun `test containsAbbaOutsideAndNoAbbaInsideBrackets`(expected: Boolean, s: String) =
        with(CheckLetterPairs()) {
            assertEquals(expected, containsAbbaOutsideAndNoAbbaInsideBrackets(s))
        }

    private class ArgumentsProviderCheckLetterPairs : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(true, "abba[mnop]qrst"),
            Arguments.of(false, "abcd[bddb]xyyx"),
            Arguments.of(false, "aaaa[qwer]tyui"),
            Arguments.of(true, "ioxxoj[asdfgh]zxcvbn"),
        )
    }
}