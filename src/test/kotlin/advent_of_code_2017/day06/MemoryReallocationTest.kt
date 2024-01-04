package advent_of_code_2017.day06

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class MemoryReallocationTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMemoryReallocationTotalSteps::class)
    fun `test minStepsUntilRepetition`(expected: Int, memoryBankSizes: List<Int>) = with(MemoryReallocation()) {
        assertEquals(expected, minStepsUntilRepetition(memoryBankSizes))
    }

    private class ArgumentsProviderMemoryReallocationTotalSteps : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(5, listOf(0, 2, 7, 0)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMemoryReallocationRepetitionLoop::class)
    fun `test lengthOfRepetitionLoop`(expected: Int, memoryBankSizes: List<Int>) = with(MemoryReallocation()) {
        assertEquals(expected, lengthOfRepetitionLoop(memoryBankSizes))
    }

    private class ArgumentsProviderMemoryReallocationRepetitionLoop : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(4, listOf(0, 2, 7, 0)),
        )
    }
}