package advent_of_code_2017.day18

import advent_of_code_2017.day23.CoprocessorAssembler
import advent_of_code_2017.day23.CoprocessorAssembler.Constant
import advent_of_code_2017.day23.CoprocessorAssembler.readInput
import kotlin.math.sqrt

/**
 * [Coprocessor Conflagration](https://adventofcode.com/2017/day/18)
 *
 * Emulate a given program in an assembly language that supports following instructions:
 * - `set X Y` - sets register `X` to value `Y`.
 * - `sub X Y` - subtracts value `Y` from register `X`.
 * - `mul X Y` - multiply register `X` by value `Y`.
 * - `jnz X Y` - jump with offset `Y`, if the value of the register `X` is not zero.
 *
 * ### Part 2
 * - All registers start at 0, except for register `a` that starts at 1.
 * - What is the value in register `h` after the program completes?
 *
 * Idea from [Solution by Todd Ginsberg](https://todd.ginsberg.com/post/advent-of-code/2017/day23/):
 * - The program is an inefficient way to count the number of primes between `x = b * 100 + 100_000` and `x + 17_000`,
 * skipping by 17, where `b` is the value in the first parsed instruction.
 */
class CoprocessorAssemblerEmulatorPart2PrimeNumberCounter {

    data class PrimeCountResult(val numOfPrimes: Int, val numOfNonPrimes: Int)
    fun countPrimes(ints: IntProgression): PrimeCountResult {
        val primeCount = ints.count { isPrime(it) }
        return PrimeCountResult(numOfPrimes = primeCount, numOfNonPrimes = ints.count() - primeCount)
    }


    private fun isPrime(n: Int): Boolean {
        if (n % 2 == 0) return false
        for(d in 3..sqrt(n.toDouble()).toInt() step 2) {
            if (n % d == 0) return false
        }
        return true
    }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    val instruction0 = instructions[0]
    require(instruction0 is CoprocessorAssembler.Set)
    require(instruction0.valueExpr is Constant)
    val b = instruction0.valueExpr.value
    val from = b * 100 + 100_000
    val to = from + 17_000
    val numsToCheck = from .. to step 17
    val res = CoprocessorAssemblerEmulatorPart2PrimeNumberCounter().countPrimes(numsToCheck)
    println("Number of primes and non-primes: $res")
}

fun main() {
    printResult("input.txt")
}

