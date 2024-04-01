package advent_of_code_2017.day17

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class CyclicalInsertionsTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCyclicalInsertionsInsertedElements::class)
    fun `test insertCyclically`(n: Int, k: Int, expected: List<Int>) = with(CyclicalInsertions()) {
        assertEquals(expected, insertCyclically(n, k).elements)
    }

    private class ArgumentsProviderCyclicalInsertionsInsertedElements : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, 3, listOf(0)),
            Arguments.of(1, 3, listOf(0, 1)),
            Arguments.of(2, 3, listOf(0, 2, 1)),
            Arguments.of(3, 3, listOf(0, 2, 3, 1)),
            Arguments.of(4, 3, listOf(0, 2, 4, 3, 1)),
            Arguments.of(5, 3, listOf(0, 5, 2, 4, 3, 1)),
            Arguments.of(6, 3, listOf(0, 5, 2, 4, 3, 6, 1)),
            Arguments.of(7, 3, listOf(0, 5, 7, 2, 4, 3, 6, 1)),
            Arguments.of(8, 3, listOf(0, 5, 7, 2, 4, 3, 8, 6, 1)),
            Arguments.of(9, 3, listOf(0, 9, 5, 7, 2, 4, 3, 8, 6, 1)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCyclicalInsertionsElementAfterLastInserted::class)
    fun `test getElementAfterCyclicallyInsertingNElements`(n: Int, k: Int, expected: Int) = with(CyclicalInsertions()) {
        assertEquals(expected, getElementAfterCyclicallyInsertingNElements(n, k))
    }

    private class ArgumentsProviderCyclicalInsertionsElementAfterLastInserted : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, 3, 0),
            Arguments.of(1, 3, 0),
            Arguments.of(2, 3, 1),
            Arguments.of(3, 3, 1),
            Arguments.of(4, 3, 3),
            Arguments.of(5, 3, 2),
            Arguments.of(6, 3, 1),
            Arguments.of(7, 3, 2),
            Arguments.of(8, 3, 6),
            Arguments.of(9, 3, 5),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCyclicalInsertionsElementAtOneAfterLastInserted::class)
    fun `test getElementAtOneAfterCyclicallyInsertingNElements`(n: Int, k: Int, expected: Int) =
        with(CyclicalInsertions()) {
            assertEquals(expected, getElementAtOneAfterCyclicallyInsertingNElements(n, k))
        }

    private class ArgumentsProviderCyclicalInsertionsElementAtOneAfterLastInserted : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(1, 3, 1),
            Arguments.of(2, 3, 2),
            Arguments.of(3, 3, 2),
            Arguments.of(4, 3, 2),
            Arguments.of(5, 3, 5),
            Arguments.of(6, 3, 5),
            Arguments.of(7, 3, 5),
            Arguments.of(8, 3, 5),
            Arguments.of(9, 3, 9),
            Arguments.of(10, 3, 9),
            Arguments.of(100, 3, 16),
            Arguments.of(200, 3, 16),
            Arguments.of(1000, 3, 517),
            Arguments.of(10_000, 3, 6890),
            Arguments.of(20_000, 3, 16332),
            Arguments.of(100_000, 3, 38713),
        )
    }

}
