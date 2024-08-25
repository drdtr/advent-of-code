package advent_of_code_2017.day21

import advent_of_code_2017.day21.FractalArt.*
import advent_of_code_2017.day21.Squares.Square
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class FractalArtTest {

    @ParameterizedTest(name = "{index}: repetitions={0}, expected={1}, startSquare={2}, rules={3}")
    @ArgumentsSource(ArgumentsProviderEnhanceMatrix::class)
    fun `test enhanceMatrix`(
        repetitions: Int,
        expected: Square,
        startSquare: Square,
        rules: Collection<SquareTransformationRule>,
    ) = with(FractalArt()) {
        assertEquals(expected, enhanceMatrix(rules, startSquare, repetitions))
    }

    private class ArgumentsProviderEnhanceMatrix : ArgumentsProvider {
        private val startingSquare = Square(
            listOf(
                listOf(0, 1, 0),
                listOf(0, 0, 1),
                listOf(1, 1, 1),
            ).map { it.intToBooleanList() },
        )

        private val rules = listOf(
            SquareTransformationRule(
                src = Square(
                    listOf(
                        listOf(0, 0),
                        listOf(0, 1),
                    ).map { it.intToBooleanList() },
                ),
                target = Square(
                    listOf(
                        listOf(1, 1, 0),
                        listOf(1, 0, 0),
                        listOf(0, 0, 0),
                    ).map { it.intToBooleanList() },
                ),
            ),
            SquareTransformationRule(
                src = Square(
                    listOf(
                        listOf(0, 1, 0),
                        listOf(0, 0, 1),
                        listOf(1, 1, 1),
                    ).map { it.intToBooleanList() },
                ),
                target = Square(
                    listOf(
                        listOf(1, 0, 0, 1),
                        listOf(0, 0, 0, 0),
                        listOf(0, 0, 0, 0),
                        listOf(1, 0, 0, 1),
                    ).map { it.intToBooleanList() },
                ),
            ),
        )

        override fun provideArguments(p0: ExtensionContext?): Stream<Arguments> {
            return Stream.of(
                arguments(
                    0,
                    Square(
                        listOf(
                            listOf(0, 1, 0),
                            listOf(0, 0, 1),
                            listOf(1, 1, 1),
                        ).map { it.intToBooleanList() },
                    ),
                    startingSquare,
                    rules,
                ),
                arguments(
                    1,
                    Square(
                        listOf(
                            listOf(1, 0, 0, 1),
                            listOf(0, 0, 0, 0),
                            listOf(0, 0, 0, 0),
                            listOf(1, 0, 0, 1),
                        ).map { it.intToBooleanList() },
                    ),
                    startingSquare,
                    rules,
                ),
                arguments(
                    2,
                    Square(
                        listOf(
                            listOf(1, 1, 0, 1, 1, 0),
                            listOf(1, 0, 0, 1, 0, 0),
                            listOf(0, 0, 0, 0, 0, 0),
                            listOf(1, 1, 0, 1, 1, 0),
                            listOf(1, 0, 0, 1, 0, 0),
                            listOf(0, 0, 0, 0, 0, 0),
                        ).map { it.intToBooleanList() },
                    ),
                    startingSquare,
                    rules,
                ),
            )
        }
    }


    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMatrixToSquare::class)
    fun `test matrixToSquare`(listMatrix: List<List<Int>>, expected: Square) = with(Squares) {
        val matrix = listMatrix.map { it.intToBooleanArray() }.toTypedArray()
        assertEquals(expected, matrix.toSquare())
    }

    private class ArgumentsProviderMatrixToSquare : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                listOf(
                    listOf(1, 0),
                    listOf(1, 0),
                ),
                Square(
                    listOf(
                        listOf(1, 0),
                        listOf(1, 0),
                    ).map { it.intToBooleanList() },
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0),
                    listOf(0, 1),
                ),
                Square(
                    listOf(
                        listOf(1, 0),
                        listOf(0, 1),
                    ).map { it.intToBooleanList() },
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0, 1),
                    listOf(1, 1, 0),
                    listOf(0, 1, 1),
                ),
                Square(
                    listOf(
                        listOf(1, 0, 1),
                        listOf(1, 1, 0),
                        listOf(0, 1, 1),
                    ).map { it.intToBooleanList() },
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0, 1, 1),
                    listOf(1, 1, 0, 0),
                    listOf(0, 1, 1, 0),
                    listOf(1, 0, 1, 1),
                ),
                Square(
                    listOf(
                        listOf(1, 0, 1, 1),
                        listOf(1, 1, 0, 0),
                        listOf(0, 1, 1, 0),
                        listOf(1, 0, 1, 1),
                    ).map { it.intToBooleanList() },
                ),
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMatrixToSquareFlipped::class)
    fun `test matrixToSquareFlipped`(listMatrix: List<List<Int>>, expected: Square) = with(Squares) {
        val matrix = listMatrix.map { it.intToBooleanArray() }.toTypedArray()
        assertEquals(expected, matrix.toSquareFlipped())
    }

    private class ArgumentsProviderMatrixToSquareFlipped : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                listOf(
                    listOf(1, 0),
                    listOf(1, 0),
                ),
                Square(
                    listOf(
                        listOf(1, 0),
                        listOf(1, 0),
                    ).map { it.intToBooleanList() },
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0),
                    listOf(0, 1),
                ),
                Square(
                    listOf(
                        listOf(0, 1),
                        listOf(1, 0),
                    ).map { it.intToBooleanList() },
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0, 1),
                    listOf(1, 1, 0),
                    listOf(0, 1, 1),
                ),
                Square(
                    listOf(
                        listOf(0, 1, 1),
                        listOf(1, 1, 0),
                        listOf(1, 0, 1),
                    ).map { it.intToBooleanList() },
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0, 1, 1),
                    listOf(0, 1, 1, 0),
                    listOf(1, 1, 0, 0),
                    listOf(1, 0, 1, 1),
                ),
                Square(
                    listOf(
                        listOf(1, 0, 1, 1),
                        listOf(1, 1, 0, 0),
                        listOf(0, 1, 1, 0),
                        listOf(1, 0, 1, 1),
                    ).map { it.intToBooleanList() },
                ),
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderRotateMatrix::class)
    fun `test rotateMatrix`(listMatrix: List<List<Int>>, expected: List<List<Int>>) = with(Squares) {
        val matrix = listMatrix.map { it.intToBooleanArray() }.toTypedArray()
        rotate(matrix)
        assertEquals(expected, matrix.map { it.booleanToIntList() })
    }

    private class ArgumentsProviderRotateMatrix : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                listOf(
                    listOf(1, 0),
                    listOf(1, 0),
                ),
                listOf(
                    listOf(1, 1),
                    listOf(0, 0),
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 1),
                    listOf(0, 0),
                ),
                listOf(
                    listOf(0, 1),
                    listOf(0, 1),
                ),
            ),
            arguments(
                listOf(
                    listOf(0, 1),
                    listOf(0, 1),
                ),
                listOf(
                    listOf(0, 0),
                    listOf(1, 1),
                ),
            ),
            arguments(
                listOf(
                    listOf(0, 0),
                    listOf(1, 1),
                ),
                listOf(
                    listOf(1, 0),
                    listOf(1, 0),
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 1, 0),
                    listOf(0, 1, 1),
                    listOf(0, 0, 1),
                ),
                listOf(
                    listOf(0, 0, 1),
                    listOf(0, 1, 1),
                    listOf(1, 1, 0),
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 0, 1),
                    listOf(1, 1, 0),
                    listOf(1, 0, 1),
                ),
                listOf(
                    listOf(1, 1, 1),
                    listOf(0, 1, 0),
                    listOf(1, 0, 1),
                ),
            ),
            arguments(
                listOf(
                    listOf(1, 1, 0, 1),
                    listOf(0, 1, 1, 0),
                    listOf(0, 0, 1, 1),
                    listOf(1, 0, 0, 0),
                ),
                listOf(
                    listOf(1, 0, 0, 1),
                    listOf(0, 0, 1, 1),
                    listOf(0, 1, 1, 0),
                    listOf(0, 1, 0, 1),
                ),
            ),
        )
    }

    companion object {
        private fun List<Int>.intToBooleanList() = map { it != 0 }
        private fun List<Int>.intToBooleanArray() = map { it != 0 }.toBooleanArray()

        private fun BooleanArray.booleanToIntList() = map { if (it) 1 else 0 }
    }
}