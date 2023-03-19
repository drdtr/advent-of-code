package advent_of_code_2015.day12

import Util.readInputLines
import org.json.JSONArray
import org.json.JSONObject

/**
 * [JSAbacusFramework.io - Part 2](https://adventofcode.com/2015/day/12#part2)
 *
 * For a given Json document, return the sum of all numbers in the document,
 * ignoring any objects (with children), but nor arrays, that have a property with the value `red`.
 *
 */
class SumOfParsedNumbersPart2 {
    fun sumOfParsedNumbers(jsonStr: String, excludeValues: Set<String>): Int =
        try {
            JSONObject(jsonStr).toMap()
        } catch (e: Exception) {
            JSONArray(jsonStr).toList()
        }.sumRecExcluding(excludeValues)

    private fun Any?.sumRecExcluding(excludeValues: Set<String>) = when (this) {
        is Int           -> this
        is Map<*, *>     -> sumRecExcluding(excludeValues)
        is Collection<*> -> sumRecExcluding(excludeValues)
        else             -> 0
    }

    private fun Map<*, *>.sumRecExcluding(excludeValues: Set<String>): Int = with(values) {
        // we exclude json objects where at least one value is contained in excludeValues
        if (any { it in excludeValues }) 0 else sumRecExcluding(excludeValues)
    }

    private fun Collection<*>.sumRecExcluding(excludeValues: Set<String>): Int =
        // we don't exclude json arrays, but pass the excludeValues down the tree for possible json objects
        sumOf { value -> value.sumRecExcluding(excludeValues) }
}

private fun readInput(inputFileName: String): String {
    return readInputLines(2015, 12, inputFileName)[0]
}

private fun printResult(inputFileName: String) {
    val s = readInput(inputFileName)
    val solver = SumOfParsedNumbersPart2()
    var sum = solver.sumOfParsedNumbers(s, setOf("red"))
    println("Sum of all parsed numbers: $sum")
}

fun main() {
    printResult("input.txt")
}