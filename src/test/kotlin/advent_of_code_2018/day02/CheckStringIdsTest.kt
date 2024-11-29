package advent_of_code_2018.day02

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class CheckStringIdsTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderChecksumOfRepeatedLetters::class)
    fun `test calcChecksumOfRepeatedLetters`(expected: Int, strings: List<String>) =
        with(CheckStringIds()) {
            assertEquals(expected, calcChecksumOfRepeatedLetters(strings))
        }

    private class ArgumentsProviderChecksumOfRepeatedLetters : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            Arguments.arguments(
                4 * 3,
                listOf(
                    "abcdef",
                    "bababc",
                    "abbcde",
                    "abcccd",
                    "aabcdd",
                    "abcdee",
                    "ababab",
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCommonLettersInTwoStringsDifferingByExactlyOneLetter::class)
    fun `test getCommonLettersInTwoStringsDifferingByExactlyOneLetter`(expected: String, strings: List<String>) =
        with(CheckStringIds()) {
            assertEquals(expected, getCommonLettersInTwoStringsDifferingByExactlyOneLetter(strings))
        }

    private class ArgumentsProviderCommonLettersInTwoStringsDifferingByExactlyOneLetter : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            Arguments.arguments(
                "fgij",
                listOf(
                    "abcde",
                    "fghij",
                    "klmno",
                    "pqrst",
                    "fguij",
                    "axcye",
                    "wvxyz",
                )
            ),
        )
    }
}