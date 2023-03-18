package advent_of_code_2015.day11

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class NextPasswordTest {


    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderNextValidPassword::class)
    fun `test nextValidPassword`(s: String, expected: String) = with (NextPassword()){
        assertEquals(expected, nextValidPassword(s))
    }

    private class ArgumentsProviderNextValidPassword : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("abcdefgh", "abcdffaa"),
            Arguments.of("ghijklmn", "ghjaabcc"),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderIsValidPassword::class)
    fun `test isValidPassword`(s: String, expected: Boolean) = with (NextPassword()){
        assertEquals(expected, isValidPassword(s.toCharArray()))
    }

    private class ArgumentsProviderIsValidPassword : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("hijklmmn", false),
            Arguments.of("abbceffg", false),
            Arguments.of("abbcegjk", false),
            Arguments.of("abcdffaa", true),
            Arguments.of("ghjaabcc", true),
        )
    }
}