package advent_of_code_2015.day01

import Util.readInputLines

/**
 * [Not Quite Lisp](https://adventofcode.com/2015/day/1)
 *
 * A string of parentheses denotes the way to a floor in a building, where `(` means `up` and `)` means `down`.
 * Return the resulting floor and the index of the first character reaching basement, i.e., reaching `floor == -1`.
 *
 * Examples:
 * - `(())` and `()()` result in floor 0
 * - `(((` and `(()(()(` result in floor 3
 *
 */
class CalculateFloor {
    data class CalcFloorResult(val lastFloor: Int, val firstIndexReachingBasement: Int)

    fun calcFloor(s: String): CalcFloorResult {
        var floor = 0
        var indexReachingBasement = -1

        s.forEachIndexed { index, ch ->
            if (ch == '(') floor++ else floor--
            if (indexReachingBasement < 0 && floor == -1) indexReachingBasement = index
        }

        return CalcFloorResult(floor, indexReachingBasement)
    }
}

private fun printResult(inputFileName: String) {
    val s = readInputLines(2015, 1, inputFileName).first()
    val res = CalculateFloor().calcFloor(s)
    println(res)
    println("1-based index: ${res.firstIndexReachingBasement + 1}")
}

fun main() {
    printResult("input.txt")
}