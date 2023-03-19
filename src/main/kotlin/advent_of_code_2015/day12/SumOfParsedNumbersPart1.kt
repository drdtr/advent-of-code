package advent_of_code_2015.day12

import Util.readInputLines

/**
 * [JSAbacusFramework.io - Part 1](https://adventofcode.com/2015/day/12)
 *
 * For a given Json document, where strings are guaranteed not to contain numbers,
 * parse all numbers in the document and return their sum.
 */
class SumOfParsedNumbersPart1 {
    fun sumOfParsedNumbers(jsonStr: String): Int = NumParser(jsonStr).parseNumbers().sum()

    private class NumParser(val s: String) {
        private val nums = mutableListOf<Int>()
        var i = 0

        fun parseNumbers(): List<Int> {
            while (i < s.length) {
                if (s[i] in '0'..'9') nums.add(parseNextNum())
                i++
            }
            return nums
        }

        private fun parseNextNum(): Int {
            val sign = if (i > 0 && s[i - 1] == '-') -1 else 1
            var n = 0
            do {
                n = 10 * n + (s[i] - '0')
            } while (s[++i] in '0'..'9')

            return n * sign
        }
    }
}


private fun readInput(inputFileName: String): String {
    return readInputLines(2015, 12, inputFileName)[0]
}

private fun printResult(inputFileName: String) {
    val s = readInput(inputFileName)
    val solver = SumOfParsedNumbersPart1()
    var sum = solver.sumOfParsedNumbers(s)
    println("Sum of all parsed numbers: $sum")
}

fun main() {
    printResult("input.txt")
}