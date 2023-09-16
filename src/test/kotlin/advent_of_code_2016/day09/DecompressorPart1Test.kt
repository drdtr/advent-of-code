package advent_of_code_2016.day09

import advent_of_code_2016.day08.MatrixPixelShiftTest.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class DecompressorPart1Test {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDecompressor::class)
    fun `test Decompressor`(expected: Int, s: String) = with(DecompressorPart1()) {
        assertEquals(expected, calcDecompressedLength(s))
    }

    private class ArgumentsProviderDecompressor : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(6, "ADVENT"),
            Arguments.of(7, "A(1x5)BC"),
            Arguments.of(9, "(3x3)XYZ"),
            Arguments.of(11, "A(2x2)BCD(2x2)EFG"),
            Arguments.of(6, "(6x1)(1x3)A"),
            Arguments.of(18, "X(8x2)(3x3)ABCY"),
        )
    }
}