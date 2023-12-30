package advent_of_code_2017.day01

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class SumOfMatchingDigitsTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSumOfDigitsMatchingNextDigit::class)
    fun `test sumOfMatchingDigits, next digit`(expected: Int, digitStr: String) = with(SumOfMatchingDigits()) {
        assertEquals(expected, sumOfMatchingDigits(digitStr, shift = 1))
    }

    private class ArgumentsProviderSumOfDigitsMatchingNextDigit : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, ""),
            Arguments.of(0, "1"),
            Arguments.of(2, "11"),
            Arguments.of(1, "112"),
            Arguments.of(1, "121"),
            Arguments.of(3, "1122"),
            Arguments.of(4, "1111"),
            Arguments.of(0, "1234"),
            Arguments.of(0, "1213"),
            Arguments.of(9, "91212129"),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSumOfDigitsMatchingDigitHalfwayAround::class)
    fun `test sumOfMatchingDigits, digit halfway around`(expected: Int, digitStr: String) =
        with(SumOfMatchingDigits()) {
            assertEquals(expected, sumOfMatchingDigits(digitStr, shift = digitStr.length / 2))
        }

    private class ArgumentsProviderSumOfDigitsMatchingDigitHalfwayAround : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, ""),
            Arguments.of(0, "1"),
            Arguments.of(2, "11"),
            Arguments.of(0, "1122"),
            Arguments.of(4, "1111"),
            Arguments.of(0, "1234"),
            Arguments.of(2, "1213"),
            Arguments.of(6, "1212"),
            Arguments.of(4, "12131415"),
        )
    }
}