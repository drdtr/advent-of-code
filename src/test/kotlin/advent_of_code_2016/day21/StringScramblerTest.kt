package advent_of_code_2016.day21

import advent_of_code_2016.day21.StringScrambler.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class StringScramblerTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderStringScrambler::class)
    fun `test scramble`(scrambled: String, unscrambled: String, ops: List<ScrambleOp>) =
        with(StringScrambler()) {
            assertEquals(scrambled, scramble(unscrambled, ops))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderStringScrambler::class)
    fun `test unscramble`(scrambled: String, unscrambled: String, ops: List<ScrambleOp>) =
        with(StringScrambler()) {
            assertEquals(unscrambled, unscramble(scrambled, ops))
        }

    private class ArgumentsProviderStringScrambler : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            arguments(
                "ebcda", "abcde", listOf(
                    Swap(0, 4),
                )
            ),
            arguments(
                "edcba", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                )
            ),
            arguments(
                "abcde", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                    ReverseBetween(0, 4),
                )
            ),
            arguments(
                "bcdea", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                    ReverseBetween(0, 4),
                    RotateLeft(1),
                )
            ),
            arguments(
                "bdeac", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                    ReverseBetween(0, 4),
                    RotateLeft(1),
                    Move(1, 4),
                )
            ),
            arguments(
                "abdec", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                    ReverseBetween(0, 4),
                    RotateLeft(1),
                    Move(1, 4),
                    Move(3, 0),
                )
            ),
            arguments(
                "ecabd", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                    ReverseBetween(0, 4),
                    RotateLeft(1),
                    Move(1, 4),
                    Move(3, 0),
                    RotateRightByIndexOf('b'),
                )
            ),
            arguments(
                "decab", "abcde", listOf(
                    Swap(0, 4),
                    SwapLetters('d', 'b'),
                    ReverseBetween(0, 4),
                    RotateLeft(1),
                    Move(1, 4),
                    Move(3, 0),
                    RotateRightByIndexOf('b'),
                    RotateRightByIndexOf('d'),
                )
            ),
        )
    }
}