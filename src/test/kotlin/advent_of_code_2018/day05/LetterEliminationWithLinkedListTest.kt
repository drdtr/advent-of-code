package advent_of_code_2018.day05

import advent_of_code_2018.day05.LetterEliminationWithStack.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class LetterEliminationWithLinkedListTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderLetterElimination::class)
    fun `test eliminateLetterPairs, with linked list`(expected: String, str: String) =
        with(LetterEliminationWithLinkedList()) {
            assertEquals(expected, eliminateLetterPairs(str))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderLetterElimination::class)
    fun `test eliminateLetterPairs, with stack`(expected: String, str: String) =
        with(LetterEliminationWithStack()) {
            assertEquals(expected, eliminateLetterPairs(str))
        }

    private class ArgumentsProviderLetterElimination : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments("", "aA"),
            arguments("", "Aa"),
            arguments("", "AabB"),
            arguments("", "aBbA"),
            arguments("abAB", "abAB"),
            arguments("aabAAB", "aabAAB"),
            arguments("", "aabBAA"),
            arguments("dabCBAcaDA", "dabAcCaCBAcCcaDA"),
            arguments("daeFFFbCBAcaDA", "daefFFFFffFFbAcCaCBAcCcaDA"),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMaxLetterElimination::class)
    fun `test maxEliminateLetterPairsAfterDroppingAllOfOneLetter, with stack`(
        expected: Pair<Char, String>,
        str: String,
    ) =
        with(LetterEliminationWithStack()) {
            assertEquals(
                expected.let { (droppedLetter, s) -> MaxEliminateLetterPairsResult(droppedLetter, s) },
                shortestAfterDroppingAllOfOneLetterAndEliminatingLetterPairs(str)
            )
        }


    private class ArgumentsProviderMaxLetterElimination : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments('a' to "", "aA"),
            arguments('a' to "", "Aa"),
            arguments('a' to "bb", "bAAAAAAAAAAAAAAAAb"),
            arguments('a' to "", "bAAAAAAAaAAAAAAAAAB"),
            arguments('c' to "daDA", "dabAcCaCBAcCcaDA"),
        )
    }

}