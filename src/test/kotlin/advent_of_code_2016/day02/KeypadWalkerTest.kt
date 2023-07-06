package advent_of_code_2016.day02

import advent_of_code_2016.day01.StreetGridDistance.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class KeypadWalkerTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderKeypad3::class)
    fun `test calculateCodeOnKeypad3_3_3`(expected: String, stepStrings: List<String>) = with(KeypadWalker()) {
        assertEquals(expected, calculateCodeOnKeypad3_3_3(stepStrings))
    }

    private class ArgumentsProviderKeypad3 : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                "1985", listOf(
                    "ULL",
                    "RRDDD",
                    "LURDL",
                    "UUUUD",
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderKeypad5::class)
    fun `test calculateCodeOnKeypad1_3_5_3_1`(expected: String, stepStrings: List<String>) = with(KeypadWalker()) {
        assertEquals(expected, calculateCodeOnKeypad1_3_5_3_1(stepStrings))
    }

    private class ArgumentsProviderKeypad5 : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                "5DB3", listOf(
                    "ULL",
                    "RRDDD",
                    "LURDL",
                    "UUUUD",
                )
            ),
        )
    }

}