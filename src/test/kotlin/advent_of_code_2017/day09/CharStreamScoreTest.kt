package advent_of_code_2017.day09

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class CharStreamScoreTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCalcTotalScore::class)
    fun `test calcTotalScore`(expected: Int, s: String) =
        with(CharStreamScore()) { assertEquals(expected, calcTotalScore(s)) }

    private class ArgumentsProviderCalcTotalScore : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(1, "{}"),
            Arguments.of(3, "{{}}"),
            Arguments.of(6, "{{{}}}"),
            Arguments.of(5, "{{},{}}"),
            Arguments.of(16, "{{{},{},{{}}}}"),
            Arguments.of(1, "{<a>,<a>,<a>,<a>}"),
            Arguments.of(9, "{{<ab>},{<ab>},{<ab>},{<ab>}}"),
            Arguments.of(9, "{{<!!>},{<!!>},{<!!>},{<!!>}}"),
            Arguments.of(3, "{{<a!>},{<a!>},{<a!>},{<ab>}}"),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCalcRemovedGarbageChars::class)
    fun `test calcRemovedGarbage`(expected: Int, s: String) =
        with(CharStreamScore()) { assertEquals(expected, countRemovedGarbageChars(s)) }

    private class ArgumentsProviderCalcRemovedGarbageChars : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, "{<>}"),
            Arguments.of(17, "{<random characters>}"),
            Arguments.of(3, "{<<<<>}"),
            Arguments.of(2, "{<{!>}>}"),
            Arguments.of(0, "{<!!>}"),
            Arguments.of(0, "{<!!!>>}"),
            Arguments.of(10, "{<{o\"i!a,<{i<a>}"),
        )
    }

}