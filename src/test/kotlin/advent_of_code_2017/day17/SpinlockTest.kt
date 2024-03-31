package advent_of_code_2017.day17

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class SpinlockTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSpinlockInsertedElements::class)
    fun `test insertCyclically`(n: Int, k: Int, expected: List<Int>) = with(Spinlock()) {
        assertEquals(expected, insertCyclically(n, k).elements) }

    private class ArgumentsProviderSpinlockInsertedElements : ArgumentsProvider {
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
    @ArgumentsSource(ArgumentsProviderSpinlockElementAfterLastInserted::class)
    fun `test getElementAfterCyclicalInsertingNElements`(n: Int, k: Int, expected: Int) = with(Spinlock()) {
        assertEquals(expected, getElementAfterCyclicallyInsertingNElements(n, k)) }

    private class ArgumentsProviderSpinlockElementAfterLastInserted : ArgumentsProvider {
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

}
