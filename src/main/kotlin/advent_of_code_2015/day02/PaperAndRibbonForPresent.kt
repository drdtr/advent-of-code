package advent_of_code_2015.day02

import Util.readInputLines
import advent_of_code_2015.day02.PaperAndRibbonForPresent.*

/**
 * [I Was Told There Would Be No Math](https://adventofcode.com/2015/day/2)
 *
 * You're given list of dimensions of presents in the format `<length>x<width>x<height>`.
 *
 * The _paper_ needed to wrap a present with dimensions `l`, `w`, `h` is calculated as sum of:
 * - surface area of the box: `2 * (l * w + w * h + h * l)`
 * - area of the smallest face: `min(l * w, w * h, h * l)`
 *
 * The _ribbon_ needed to wrap a present with dimensions `l`, `w`, `h` is calculated as sum of:
 * - the smallest perimeter of any face: `2 * min(l + w, w + h, h + l)`
 * - length of ribbon equal to the volume of the box, to make a bow: `l * w * h`
 *
 * Calculate how much wrapping paper and ribbon is needed for all presents.
 */
class PaperAndRibbonForPresent {
    data class Box(val length: Int, val width: Int, val height: Int)

    fun calcPaperArea(boxes: Iterable<Box>): Int = boxes.sumOf { it.paperNeeded() }

    fun calcRibbonLength(boxes: Iterable<Box>): Int = boxes.sumOf { it.ribbonNeeded() }

    private fun Box.paperNeeded(): Int {
        val faceAreaLW = length * width
        val faceAreaLH = length * height
        val faceAreaWH = width * height
        val area = 2 * (faceAreaLW + faceAreaLH + faceAreaWH)
        val minFaceArea = minOf(faceAreaLW, faceAreaLH, faceAreaWH)
        return area + minFaceArea
    }

    private fun Box.ribbonNeeded(): Int {
        val facePerimeterLW = length + width
        val facePerimeterLH = length + height
        val facePerimeterWH = width + height
        val minFacePerimeter = 2 * minOf(facePerimeterLW, facePerimeterLH, facePerimeterWH)
        val volume = length * width * height
        return minFacePerimeter + volume
    }
}

private fun readInput(inputFileName: String): List<Box> {
    val lines = readInputLines(2015, 2, inputFileName)
    return lines.asSequence()
            .map { it.split("x").map { it.toInt() } }
            .map { Box(it[0], it[1], it[2]) }
            .toList()
}

private fun printResult(inputFileName: String) {
    val boxes = readInput(inputFileName)
    val solver = PaperAndRibbonForPresent()
    println("Paper: ${solver.calcPaperArea(boxes)}")
    println("Ribbon: ${solver.calcRibbonLength(boxes)}")
}

fun main() {
    printResult("input.txt")
}