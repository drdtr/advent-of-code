package advent_of_code_2016.day08

import advent_of_code_2016.day08.MatrixPixelShift.Matrix
import advent_of_code_2016.day08.MatrixPixelShift.ShiftOp
import advent_of_code_2016.day08.MatrixPixelShift.ShiftOp.*
import advent_of_code_2016.day08.MatrixPixelShift.apply
import advent_of_code_2016.day08.MatrixPixelShift.createMatrix
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class MatrixPixelShiftTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMatrixPixelShift::class)
    fun `test MatrixPixelShift`(expected: List<List<Int>>, ops: List<ShiftOp>) {
        val matrix = createMatrix(expected.size, expected[0].size)

        matrix.apply(ops)

        assertEquals(expected, matrix.toIntList())
    }

    private fun Matrix.toIntList() =
        matrix.map { row -> row.map { if (it) 1 else 0 } }

    private class ArgumentsProviderMatrixPixelShift : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                listOf(
                    listOf(1, 1, 1, 0, 0, 0, 0),
                    listOf(1, 1, 1, 0, 0, 0, 0),
                    listOf(0, 0, 0, 0, 0, 0, 0),
                ),
                listOf(FillRect(3, 2))
            ),
            Arguments.of(
                listOf(
                    listOf(1, 0, 1, 0, 0, 0, 0),
                    listOf(1, 1, 1, 0, 0, 0, 0),
                    listOf(0, 1, 0, 0, 0, 0, 0),
                ),
                listOf(FillRect(3, 2), ShiftDown(1, 1))
            ),
            Arguments.of(
                listOf(
                    listOf(0, 0, 0, 0, 1, 0, 1),
                    listOf(1, 1, 1, 0, 0, 0, 0),
                    listOf(0, 1, 0, 0, 0, 0, 0),
                ),
                listOf(FillRect(3, 2), ShiftDown(1, 1), ShiftRight(0, 4))
            ),
            Arguments.of(
                listOf(
                    listOf(0, 1, 0, 0, 1, 0, 1),
                    listOf(1, 0, 1, 0, 0, 0, 0),
                    listOf(0, 1, 0, 0, 0, 0, 0),
                ),
                listOf(FillRect(3, 2), ShiftDown(1, 1), ShiftRight(0, 4), ShiftDown(1, 1))
            ),
        )
    }

}