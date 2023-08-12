package advent_of_code_2016.day07

import advent_of_code_2016.day06.RepetitionCoding.CharacterRestoringMode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class CheckLetterTripletsTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCheckLetterTriplets::class)
    fun `test containsAbaOutsideAndBabInsideBrackets`(expected: Boolean, s: String) =
        with(CheckLetterTriplets()) {
            assertEquals(expected, containsAbaOutsideAndBabInsideBrackets(s))
        }

    private class ArgumentsProviderCheckLetterTriplets : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(true, "aba[bab]xyz"),
            Arguments.of(false, "xyx[xyx]xyx"),
            Arguments.of(true, "aaa[kek]eke"),
            Arguments.of(true, "zazbz[bzb]cdb"),
        )
    }
}