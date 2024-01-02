package advent_of_code_2017.day04

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class PassphaseValidationTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderPassphaseValidationNoDuplicates::class)
    fun `test countValidPassphrasesContainingNoDuplicates`(expected: Int, passphrases: List<String>) =
        with(PassphaseValidation()) {
            assertEquals(expected, countValidPassphrasesContainingNoDuplicates(passphrases))
        }

    private class ArgumentsProviderPassphaseValidationNoDuplicates : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, emptyList<String>()),
            Arguments.of(0, listOf("aa aa")),
            Arguments.of(1, listOf("aa ab")),
            Arguments.of(0, listOf("aa ab aa")),
            Arguments.of(
                2, listOf(
                    "aa",
                    "aa",
                )
            ),
            Arguments.of(
                2, listOf(
                    "aa",
                    "aa ab",
                )
            ),
            Arguments.of(
                2, listOf(
                    "aa",
                    "aa ab",
                    "aa aa",
                )
            ),
            Arguments.of(
                3, listOf(
                    "aa",
                    "aa",
                    "aa ab",
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderPassphaseValidationNoAnagrams::class)
    fun `test countValidPassphrasesContainingNoAnagrams`(expected: Int, passphrases: List<String>) =
        with(PassphaseValidation()) {
            assertEquals(expected, countValidPassphrasesContainingNoAnagrams(passphrases))
        }

    private class ArgumentsProviderPassphaseValidationNoAnagrams : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, emptyList<String>()),
            Arguments.of(0, listOf("aa aa")),
            Arguments.of(1, listOf("aa ab")),
            Arguments.of(0, listOf("ab ba")),
            Arguments.of(0, listOf("aa ab ba")),
            Arguments.of(1, listOf("abcde fghij")),
            Arguments.of(0, listOf("abcde xyz ecdab")),
            Arguments.of(1, listOf("a ab abc abd abf abj")),
            Arguments.of(1, listOf("iiii oiii ooii oooi oooo")),
            Arguments.of(0, listOf("oiii ioii iioi iiio")),
            Arguments.of(
                2, listOf(
                    "aa",
                    "aa",
                )
            ),
            Arguments.of(
                2, listOf(
                    "aa",
                    "aa ab",
                )
            ),
            Arguments.of(
                2, listOf(
                    "aa",
                    "aa ab",
                    "ac ca",
                )
            ),
            Arguments.of(
                3, listOf(
                    "aa",
                    "aa",
                    "aa ab",
                )
            ),
        )
    }
}