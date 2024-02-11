package advent_of_code_2017.day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class KnotHashTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderKnotList::class)
    fun `test knotList`(expected: List<Int>, nums: List<Int>, lengths: List<Int>) = with(KnotHash()) {
        assertEquals(expected, knotList(nums, lengths))
    }

    private class ArgumentsProviderKnotList : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(listOf(0, 1, 2, 3, 4), listOf(0, 1, 2, 3, 4), listOf<Int>()),
            Arguments.of(listOf(2, 1, 0, 3, 4), listOf(0, 1, 2, 3, 4), listOf(3)),
            Arguments.of(listOf(4, 3, 0, 1, 2), listOf(0, 1, 2, 3, 4), listOf(3, 4)),
            Arguments.of(listOf(4, 3, 0, 1, 2), listOf(0, 1, 2, 3, 4), listOf(3, 4, 1)),
            Arguments.of(listOf(3, 4, 2, 1, 0), listOf(0, 1, 2, 3, 4), listOf(3, 4, 1, 5)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCalcKnotHash::class)
    fun `test calcKnotHash`(expected: Int, nums: List<Int>, lengths: List<Int>) = with(KnotHash()) {
        assertEquals(expected, calcKnotHash(nums, lengths))
    }

    private class ArgumentsProviderCalcKnotHash : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(12, listOf(0, 1, 2, 3, 4), listOf(3, 4, 1, 5)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCalcHexadecimalKnotHash::class)
    fun `test calcHexadecimalKnotHash`(expected: String, lengthsByteString: String) = with(KnotHash()) {
        val nums = (0..255).toList()
        val lengthsSuffix = listOf(17, 31, 73, 47, 23)
        val lengths = lengthsByteString.toByteArray().map { it.toInt() } + lengthsSuffix

        assertEquals(expected, calcHexadecimalKnotHash(nums, lengths, rounds = 64, numOfBlocks = 16))
    }

    private class ArgumentsProviderCalcHexadecimalKnotHash : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("a2582a3a0e66e6e86e3812dcb672a272", ""),
            Arguments.of("33efeb34ea91902bb2f59c9920caa6cd", "AoC 2017"),
            Arguments.of("3efbe78a8d82f29979031a4aa0b16a9d", "1,2,3"),
            Arguments.of("63960835bcdc130f0b66d7ff4f6a5a8e", "1,2,4"),
        )
    }
}