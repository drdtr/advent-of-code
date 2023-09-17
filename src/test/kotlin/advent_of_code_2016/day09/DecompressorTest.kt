package advent_of_code_2016.day09

import advent_of_code_2016.day08.MatrixPixelShiftTest.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class DecompressorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDecompressor::class)
    fun `test Decompressor`(expected: Long, s: String) = with(Decompressor()) {
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

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDecompressorForRecursiveMarkers::class)
    fun `test Decompressor for recursive markers`(expected: Long, s: String) = with(Decompressor()) {
        assertEquals(expected, calcDecompressedLengthForRecursiveMarkers(s))
    }

    private class ArgumentsProviderDecompressorForRecursiveMarkers : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(9, "(3x3)XYZ"),
            Arguments.of(20, "X(8x2)(3x3)ABCY"),
            Arguments.of(241920, "(27x12)(20x12)(13x14)(7x10)(1x12)A"),
            Arguments.of(445, "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"),
        )
    }
}