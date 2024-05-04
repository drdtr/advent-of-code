package advent_of_code_2016.day14

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class OneTimePadTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderOneTimePad::class)
    fun `test getNthKey`(expected: Int, salt: String, keyNumber: Int, numOfNextHashes: Int) = with(OneTimePad()) {
        assertEquals(expected, getIndexProducingNthKey(salt, keyNumber, numOfNextHashes))
    }

    private class ArgumentsProviderOneTimePad : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(39, "abc", 1, 1000),
            Arguments.of(92, "abc", 2, 1000),
            Arguments.of(22728, "abc", 64, 1000),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderOneTimePadWithHashReps::class)
    fun `test getNthKey with hash repetitions`(
        expected: Int,
        salt: String,
        keyNumber: Int,
        numOfNextHashes: Int,
        numOfMd5HashReps: Int,
    ) =
        with(OneTimePad()) {
            assertEquals(expected, getIndexProducingNthKey(salt, keyNumber, numOfNextHashes, numOfMd5HashReps))
        }

    private class ArgumentsProviderOneTimePadWithHashReps : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(22551, "abc", 64, 1000, 2017),
        )
    }


}