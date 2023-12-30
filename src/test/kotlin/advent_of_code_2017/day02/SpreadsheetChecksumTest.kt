package advent_of_code_2017.day02

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class SpreadsheetChecksumTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSpreadsheetChecksumWithMaxMinDiffs::class)
    fun `test calcSumOfMaxMinDiffs`(expected: Int, rows: List<List<Int>>) = with(SpreadsheetChecksum()) {
        assertEquals(expected, calcSumOfMaxMinDiffs(rows.map { it.toIntArray() }))
    }

    private class ArgumentsProviderSpreadsheetChecksumWithMaxMinDiffs : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                0, emptyList<List<Int>>()
            ),
            Arguments.of(
                4, listOf(
                    listOf(7, 5, 3),
                )
            ),
            Arguments.of(
                12, listOf(
                    listOf(5, 1, 9, 5),
                    listOf(7, 5, 3),
                )
            ),
            Arguments.of(
                18, listOf(
                    listOf(5, 1, 9, 5),
                    listOf(7, 5, 3),
                    listOf(2, 4, 6, 8),
                )
            ),
        )
    }


    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSpreadsheetChecksumWithQuotients::class)
    fun `test calcSumOfQuotients`(expected: Int, rows: List<List<Int>>) = with(SpreadsheetChecksum()) {
        assertEquals(expected, calcSumOfQuotients(rows.map { it.toIntArray() }))
    }

    private class ArgumentsProviderSpreadsheetChecksumWithQuotients : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                0, emptyList<List<Int>>()
            ),
            Arguments.of(
                3, listOf(
                    listOf(9, 4, 7, 3),
                )
            ),
            Arguments.of(
                7, listOf(
                    listOf(5, 9, 2, 8),
                    listOf(9, 4, 7, 3),
                )
            ),
            Arguments.of(
                9, listOf(
                    listOf(5, 9, 2, 8),
                    listOf(9, 4, 7, 3),
                    listOf(3, 8, 6, 5),
                )
            ),
        )
    }
}