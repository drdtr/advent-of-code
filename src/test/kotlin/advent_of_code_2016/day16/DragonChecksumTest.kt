package advent_of_code_2016.day16

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class DragonChecksumTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDragonChecksum::class)
    fun `test DragonChecksum`(expected: String, s: String, n: Int) = with(DragonChecksum()) {
        assertEquals(expected, calcDragonChecksum(s, n))
    }

    private class ArgumentsProviderDragonChecksum : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            // extend string test cases (checksum is the same because the extended string has odd length)
            Arguments.of("100", "1", 3),
            Arguments.of("001", "0", 3),
            Arguments.of("11111000000", "11111", 11),
            Arguments.of("1111000010100101011110000", "111100001010", 25),
            // reduce string test case (no extension because the string is already long enough)
            Arguments.of("100", "110010110100", 12),
            // extend and reduce string
            Arguments.of("01100", "10000", 20),
        )
    }

}