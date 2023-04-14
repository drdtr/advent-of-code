package advent_of_code_2015.day16

import Util.readInputLines
import advent_of_code_2015.day16.FindMatchingTuples.*
import advent_of_code_2015.day16.FindMatchingTuples.ComparisonOutcome.*
import advent_of_code_2015.day16.FindMatchingTuples.ComparisonTuple.Companion.toComparisonTuple
import advent_of_code_2015.day16.FindMatchingTuples.Tuple.Companion.toTuple

/**
 * [Aunt Sue](https://adventofcode.com/2015/day/16)
 *
 * Given a tuple of search criteria, and a list of tuples, find the tuples
 * where present/non-null properties match the search criteria, and return their indices in the input list of tuples.
 */
class FindMatchingTuples {
    enum class ComparisonOutcome { GreaterThan, Equal, LessThan }

    data class Tuple(val entries: Map<String, Int>) {
        companion object {
            fun Map<String, Int>.toTuple() = Tuple(this)
        }
    }

    data class ComparisonTuple(val entries: Map<String, Pair<ComparisonOutcome, Int>>) {
        companion object {
            fun Map<String, Pair<ComparisonOutcome, Int>>.toComparisonTuple() = ComparisonTuple(this)
        }
    }

    fun indicesMatching(tuples: List<Tuple>, criteria: Tuple): List<Int> =
        tuples.mapIndexedNotNull { i, t -> if (t.matches(criteria)) i else null }

    private fun Tuple.matches(criteria: Tuple): Boolean =
        entries.all { (k, v) -> criteria.entries[k].let { it == null || it == v } }

    fun indicesMatchingComparison(tuples: List<Tuple>, criteria: ComparisonTuple): List<Int> =
        tuples.mapIndexedNotNull { i, t -> if (t.matchesComparison(criteria)) i else null }

    private fun Tuple.matchesComparison(criteria: ComparisonTuple): Boolean =
        entries.all { (k, v) -> criteria.entries[k].let { it == null || matchesComparison(v, it.first, it.second) } }

    private fun matchesComparison(value: Int, comparisonOutcome: ComparisonOutcome, searchValue: Int) =
        when (comparisonOutcome) {
            GreaterThan -> value > searchValue
            LessThan    -> value < searchValue
            Equal       -> value == searchValue
        }
}

//private typealias ComparisonTuple = Map<String, Pair<ComparisonOutcome, Int>>


private fun readInput(inputFileName: String): List<Tuple> =
    readInputLines(2015, 16, inputFileName).map { parseTuple(it) }

private fun parseTuple(s: String): Tuple {
    val split = s.split(" ", ",", ":")
    return (3 until split.size step 4).associate { i -> split[i] to split[i + 2].toInt() }.toTuple()
}

private fun printResult(inputFileName: String) {
    val tuples = readInput(inputFileName)
    val solver = FindMatchingTuples()
    printResultPart1(solver, tuples)
    printResultPart2(solver, tuples)
}

private fun printResultPart1(solver: FindMatchingTuples, tuples: List<Tuple>) {
    val criteria = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    ).toTuple()
    val res = solver.indicesMatching(tuples, criteria)
    println("Part 1: Matching indices (0-based): $res")
    println("Part 1: Matching indices (1-based): ${res.map { it + 1 }}")

}

private fun printResultPart2(solver: FindMatchingTuples, tuples: List<Tuple>) {
    val criteria = mapOf(
        "children" to (Equal to 3),
        "cats" to (GreaterThan to 7),
        "samoyeds" to (Equal to 2),
        "pomeranians" to (LessThan to 3),
        "akitas" to (Equal to 0),
        "vizslas" to (Equal to 0),
        "goldfish" to (LessThan to 5),
        "trees" to (GreaterThan to 3),
        "cars" to (Equal to 2),
        "perfumes" to (Equal to 1),
    ).toComparisonTuple()
    val res = solver.indicesMatchingComparison(tuples, criteria)
    println("Part 2: Matching indices (0-based): $res")
    println("Part 2: Matching indices (1-based): ${res.map { it + 1 }}")

}

fun main() {
    printResult("input.txt")
}