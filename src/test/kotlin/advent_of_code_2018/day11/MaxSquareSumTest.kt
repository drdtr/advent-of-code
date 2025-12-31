package advent_of_code_2018.day11

import advent_of_code_2018.day11.MaxSquareSum.*
import advent_of_code_2018.day11.MaxSquareSumTest.ArgumentsProviders.ArgumentsProviderMaxSumOf3x3Square
import advent_of_code_2018.day11.MaxSquareSumTest.ArgumentsProviders.ArgumentsProviderMaxSumOfAnySquare
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Named.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class MaxSquareSumTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMaxSumOf3x3Square::class)
    fun `test findMaxSumOf3x3Square`(expected: MaxSumOf3x3SquareResult, k: Int, gridWidth: Int, gridHeight: Int) =
        with(MaxSquareSum()) {
            assertEquals(expected, findMaxSumOf3x3Square(gridWidth = gridWidth, gridHeight = gridHeight, k = k))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMaxSumOfAnySquare::class)
    fun `test findMaxSumOfAnySquare`(expected: MaxSumOfSquareResult, k: Int, gridWidth: Int, gridHeight: Int) =
        with(MaxSquareSum()) {
            assertEquals(expected, findMaxSumOfAnySquare(gridWidth = gridWidth, gridHeight = gridHeight, k = k))
        }


    private object ArgumentsProviders {
        class ArgumentsProviderMaxSumOf3x3Square : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(
                    named("expected", MaxSumOf3x3SquareResult(sum = 29, x = 33, y = 45)),
                    named("k", 18), named("gridWidth", 300), named("gridHeight", 300)
                ),
                arguments(
                    named("expected", MaxSumOf3x3SquareResult(sum = 30, x = 21, y = 61)),
                    named("k", 42), named("gridWidth", 300), named("gridHeight", 300)
                ),
            )
        }

        class ArgumentsProviderMaxSumOfAnySquare : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(
                    named("expected", MaxSumOfSquareResult(sum = 113, x = 90, y = 269, size = 16)),
                    named("k", 18), named("gridWidth", 300), named("gridHeight", 300)
                ),
                arguments(
                    named("expected", MaxSumOfSquareResult(sum = 119, x = 232, y = 251, size = 12)),
                    named("k", 42), named("gridWidth", 300), named("gridHeight", 300)
                ),
            )
        }
    }
}