package advent_of_code_2017.day21

import Util.readInputLines
import advent_of_code_2017.day21.FractalArt.*
import advent_of_code_2017.day21.Squares.Square
import advent_of_code_2017.day21.Squares.countPixels
import advent_of_code_2017.day21.Squares.rotate
import advent_of_code_2017.day21.Squares.toMatrix
import advent_of_code_2017.day21.Squares.toSquare
import advent_of_code_2017.day21.Squares.toSquareFlipped
import java.util.regex.Pattern

/**
 * [Fractal Art](https://adventofcode.com/2017/day/21)
 *
 * You're given a `3 * 3` matrix. You apply rules transforming a `2 * 2` or `3 * 3` square
 * into a `3 * 3` or `4 * 4` square, respectively. The `2 * 2` are applied when the matrix size is even,
 * and the `3 * 3` rules are applied when the matrix size is divisible by 3.
 *
 * A rule matches any rotation or flip of the left-side square,
 * but the resulting right-side square is always the same.
 */
class FractalArt {
    /**
     * Square transformation rule denoting that a square `src` would be replaced by the square `target`.
     *
     * Only valid if the source square has size 2 or 3 and the `target` square has size of one more,
     * i.e. 3 or 4, respectively.
     *
     */
    data class SquareTransformationRule(val src: Square, val target: Square) {
        init {
            require((src.sideLength == 2 || src.sideLength == 3) && target.sideLength == src.sideLength + 1) {
                "Src square must have side length 2 or 3 and target square must have side length of one more than src."
            }
        }

        override fun toString(): String {
            return "$src\n⇓\n$target"
        }
    }

    fun enhanceMatrix(rules: Collection<SquareTransformationRule>, startSquare: Square, repetitions: Int = 1): Square {
        if (repetitions <= 0) return startSquare

        val rulesBySrc: Map<Square, SquareTransformationRule> = rules.associateBy { it.src }
        var matrix = startSquare.toMatrix()
        repeat(repetitions) {
            matrix = enhanceMatrixInternal(matrix, rulesBySrc)
        }
        return matrix.toSquare()
    }


    private fun enhanceMatrixInternal(
        matrix: Array<BooleanArray>,
        rulesBySrc: Map<Square, SquareTransformationRule>
    ): Array<BooleanArray> {
        val srcSquareSize = when {
            matrix.size % 2 == 0 -> 2
            matrix.size % 3 == 0 -> 3
            else                 -> error("Matrix size must be divisible by 2 or 3, but was ${matrix.size}")
        }
        val targetSquareSize = srcSquareSize + 1
        val resMatrixSize = matrix.size / srcSquareSize * targetSquareSize
        val resMatrix = Array(resMatrixSize) { BooleanArray(resMatrixSize) }

        for (rowSquareIdx in 0 until matrix.size / srcSquareSize) {
            for (colSquareIdx in 0 until matrix.size / srcSquareSize) {
                val srcSquare =
                    getSquare(matrix, rowSquareIdx * srcSquareSize, colSquareIdx * srcSquareSize, srcSquareSize)
                val rule = findMatchingRuleWithRotationsAndFlips(srcSquare, rulesBySrc)
                putSquare(resMatrix, rule.target, rowSquareIdx * targetSquareSize, colSquareIdx * targetSquareSize)
            }
        }

        return resMatrix
    }

    private fun findMatchingRuleWithRotationsAndFlips(
        srcSquare: Square,
        rulesBySrc: Map<Square, SquareTransformationRule>
    ): SquareTransformationRule {
        rulesBySrc[srcSquare]?.let { return it }

        val matrix = srcSquare.toMatrix()
        rulesBySrc[matrix.toSquareFlipped()]?.let { return it }

        repeat(3) {
            rotate(matrix)
            rulesBySrc[matrix.toSquare()]?.let { return it }
            rulesBySrc[matrix.toSquareFlipped()]?.let { return it }
        }

        error("Couldn't find a rule for square: \n$srcSquare")
    }

    private fun getSquare(matrix: Array<BooleanArray>, rowIdxFrom: Int, colIdxFrom: Int, squareSize: Int): Square {
        return Square(
            pixels = (rowIdxFrom until rowIdxFrom + squareSize).map { rowIdx ->
                matrix[rowIdx].copyOfRange(colIdxFrom, colIdxFrom + squareSize).toList()
            }
        )
    }

    private fun putSquare(matrix: Array<BooleanArray>, square: Square, rowIdxFrom: Int, colIdxFrom: Int) {
        for (squareRowIdx in 0 until square.sideLength) {
            for (squareColIdx in 0 until square.sideLength) {
                matrix[rowIdxFrom + squareRowIdx][colIdxFrom + squareColIdx] = square[squareRowIdx][squareColIdx]
            }
        }
    }
}

object Squares {
    data class Square(val pixels: List<List<Boolean>>) {
        init {
            pixels.forEach { row ->
                require(row.size == pixels.size) {
                    "All rows of a square must have the same length as the square's side, but was $pixels"
                }
            }
        }

        val sideLength = pixels.size

        override fun toString(): String =
            pixels.joinToString(prefix = "|", postfix = "|", separator = "|\n|") { row ->
                row.joinToString(separator = "") {
                    it.toPixel().toString()
                }
            }

        operator fun get(rowIdx: Int) = pixels[rowIdx]

        companion object {
            // White circle, see https://en.wikipedia.org/wiki/Geometric_Shapes_(Unicode_block)
            private const val PIXEL_OFF = '\u25CB'

            // Black block, see https://en.wikipedia.org/wiki/Block_Elements
            private const val PIXEL_ON = '\u25CF'

            private fun Boolean.toPixel() = if (this) PIXEL_ON else PIXEL_OFF
        }
    }

    fun Square.countPixels() = pixels.sumOf { row -> row.count { it } }

    fun Square.toMatrix(): Array<BooleanArray> =
        pixels.map { row -> row.toBooleanArray() }.toTypedArray()

    fun Array<BooleanArray>.toSquare(): Square =
        Square(pixels = map { row -> row.toList() })

    /**
     * Note: we flip across the horizontal axis of the matrix, i.e., swapping the rows
     * (row 0 with row n - 1, row 1 with row n - 2, ...) because it's simpler and more efficient than flipping
     * each row vertically across its middle.
     * This will still cover all possible eight modifications of a matrix, which resulting from four possible rotations
     * and from flipping because flipping a matrix across the vertical axis can also be achieved
     * by rotating a matrix 180° and then flipping it across its horizontal axis.
     */
    fun Array<BooleanArray>.toSquareFlipped(): Square =
        Square(pixels = reversed().map { row -> row.toList() })

    /** Rotates a given matrix by 90° clockwise.
     *
     * The implementation has linear runtime and uses constant additional space.
     * It reuses a solution for the LeetCode problem [Rotate Image](https://leetcode.com/problems/rotate-image/).
     */
    fun rotate(matrix: Array<BooleanArray>) {
        val n = matrix.size

        for (i in 0 until n / 2)
            for (j in i until n - 1 - i)
                swap4(matrix, i, j)
    }

    private fun swap4(m: Array<BooleanArray>, i: Int, j: Int) {
        val lastIdx = m.size - 1

        // Examples of shifting for m.size==4 and starting points (0,0), (0,1), (1,1)
        // 0,0 <- 3,0 <- 3,3 <- 0,3
        // 0,1 <- 2,0 <- 3,2 <- 1,3
        // 1,1 <- 2,1 <- 2,2 <- 1,2
        val i1 = lastIdx - j
        val i2 = lastIdx - i

        val t = m[i][j]
        m[i][j] = m[i1][i]
        m[i1][i] = m[i2][i1]
        m[i2][i1] = m[j][i2]
        m[j][i2] = t
    }
}


private val squareTransformationRuleParsingPattern = Pattern.compile("([.#/]+) => ([.#/]+)")

private fun parseSquareRow(s: String): List<Boolean> = buildList {
    for (ch in s) when (ch) {
        '.' -> add(false)
        '#' -> add(true)
        else -> error("Unexpected character $ch")
    }
}

private fun parseSquare(s: String): Square =
    Square(pixels = s.split("/").map(::parseSquareRow))

private fun parseSquareRule(s: String): SquareTransformationRule =
    with(squareTransformationRuleParsingPattern.matcher(s)) {
        require(matches()) { "Not a valid square rule: $s" }
        return SquareTransformationRule(
            src = parseSquare(group(1)),
            target = parseSquare(group(2)),
        )
    }


private fun readInput(inputFileName: String): List<SquareTransformationRule> =
    readInputLines(2017, 21, inputFileName).map { s -> parseSquareRule(s) }

private fun printResult(inputFileName: String) {
    val squareTransformationRules = readInput(inputFileName)
    val startingSquare = Square(
        listOf(
            listOf(0, 1, 0),
            listOf(0, 0, 1),
            listOf(1, 1, 1),
        ).map { row -> row.map { it != 0 } },
    )
    println(squareTransformationRules.joinToString(separator = "\n----\n"))

    val solver = FractalArt()
    // part 1
    val reps1 = 5
    val resSquare1 = solver.enhanceMatrix(squareTransformationRules, startingSquare, reps1)
    val resPixelCount1 = resSquare1.countPixels()
    println("Matrix after $reps1 rep(s):\n$resSquare1")
    println("Number of set pixels: $resPixelCount1")

    // part 2
    val reps2 = 18
    val resSquare2 = solver.enhanceMatrix(squareTransformationRules, startingSquare, reps2)
    val resPixelCount2 = resSquare2.countPixels()
    println("Matrix size after $reps2 rep(s):\n${resSquare2.sideLength}")
    println("Number of set pixels: $resPixelCount2")

}

fun main() {
    printResult("input.txt")
}