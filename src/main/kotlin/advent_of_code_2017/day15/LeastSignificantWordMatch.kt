package advent_of_code_2017.day15

import Util.readInputLines
import advent_of_code_2017.day15.LeastSignificantWordMatch.*
import java.util.regex.Pattern

/**
 * [Dueling Generators](https://adventofcode.com/2017/day/15)
 *
 * Two generators `A` and `B` generate sequences of numbers as follows:
 * - a generator starts with a given value
 * - each generator has an own factor `k`: `A` has `k=16807`, B has `k=48271`
 * - for a value `v_i`, the next value `v_{i+1}` is calculated as `v_i * k % 2147483647`
 *
 * Count how often the least significant words, i.e., the lower 16 bits of the generated numbers
 * are equal for `A` and `B` after `n` rounds.
 *
 */
class LeastSignificantWordMatch {
    class Generator(val initValue: Int, val factor: Int, val modulo: Int) {
        fun generateNext(value: Int): Int = (value.toLong() * factor % 2147483647).toInt()

        fun generateNextMatchingModulo(value: Int): Int {
            var curr = value
            do {
                curr = generateNext(curr)
            } while (curr % modulo != 0)
            return curr
        }
    }

    fun countEqualLSWs(generatorA: Generator, generatorB: Generator, numRounds: Int): Int {
        var count = 0
        var valueA = generatorA.initValue
        var valueB = generatorB.initValue
        repeat(numRounds) {
            valueA = generatorA.generateNext(valueA)
            valueB = generatorB.generateNext(valueB)
            if (valueA and 0xFFFF == valueB and 0xFFFF) {
                count++
            }
        }
        return count
    }

    fun countEqualLSWsMatchingGeneratorsModulo(generatorA: Generator, generatorB: Generator, numRounds: Int): Int {
        var count = 0
        var valueA = generatorA.initValue
        var valueB = generatorB.initValue
        repeat(numRounds) {
            valueA = generatorA.generateNextMatchingModulo(valueA)
            valueB = generatorB.generateNextMatchingModulo(valueB)
            if (valueA and 0xFFFF == valueB and 0xFFFF) {
                count++
            }
        }
        return count
    }
}

/*
Generator A starts with 679
 */

private val generatorInputMatcher = Pattern.compile("Generator (\\w) starts with (\\d+)")

private fun readInput(inputFileName: String): Map<Char, Generator> =
    readInputLines(2017, 15, inputFileName).map { line ->
        with(generatorInputMatcher.matcher(line)) {
            require(matches()) { "Invalid input line: $line" }
            val generatorLetter = group(1)[0]
            val initValue = group(2).toInt()
            val generator = when (generatorLetter) {
                'A'  -> Generator(initValue = initValue, factor = 16807, modulo = 4)
                'B'  -> Generator(initValue = initValue, factor = 48271, modulo = 8)
                else -> error("Invalid generator letter $generatorLetter")
            }
            generatorLetter to generator
        }
    }.toMap()

private fun printResult(inputFileName: String) {
    val generators = readInput(inputFileName)
    val solver = LeastSignificantWordMatch()

    // part 1
    val numRounds1 = 40_000_000
    val res1 = solver.countEqualLSWs(generators['A']!!, generators['B']!!, numRounds1)
    println("Number of equal LSWs: $res1")

    // part 2
    val numRounds2 = 5_000_000
    val res2 = solver.countEqualLSWsMatchingGeneratorsModulo(generators['A']!!, generators['B']!!, numRounds2)
    println("Number of equal LSWs matching generator's modulo: $res2")

}

fun main() {
    printResult("input.txt")
}
