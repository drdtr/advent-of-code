package advent_of_code_2017.day05

import Util.readInputLines

/**
 * [A Maze of Twisty Trampolines, All Alike](https://adventofcode.com/2017/day/5)
 *
 * For a program consisting only of a `jump x` instructions, you're given a list of jump distances `x`.
 *
 * ### Part 1
 *
 * Each instruction `jump x` increases `x` by 1 and jumps `x` steps.
 * The program is finished when a jump leads outside the list.
 *
 * Calculate how many steps it will take to finish the program.
 *
 * ### Part 2
 *
 * Now, the jump distance will be decreased by 1 (instead increased by 1), if it is greater or equal 3.
 *
 * Calculate how many steps it will take to finish the program.
 */
class JumpSimulator {
    fun countSteps(jumpDistances: List<Int>): Int {
        val currJumpDistances = jumpDistances.toMutableList()
        var stepCount = 0
        var instructionPointer = 0

        while (instructionPointer in currJumpDistances.indices) {
            stepCount++
            val dist = currJumpDistances[instructionPointer]
            currJumpDistances[instructionPointer]++
            instructionPointer += dist
        }

        return stepCount
    }

    fun countStepsDecreaseDistanceIfGeqThree(jumpDistances: List<Int>): Int {
        val currJumpDistances = jumpDistances.toMutableList()
        var stepCount = 0
        var instructionPointer = 0

        while (instructionPointer in currJumpDistances.indices) {
            stepCount++
            val dist = currJumpDistances[instructionPointer]
            currJumpDistances[instructionPointer] += if (dist < 3) 1 else -1
            instructionPointer += dist
        }

        return stepCount
    }
}

private fun readInput(inputFileName: String): List<Int> =
    readInputLines(2017, 5, inputFileName).map { it.toInt() }

private fun printResult(inputFileName: String) {
    val jumpDistances = readInput(inputFileName)
    val solver = JumpSimulator()
    // part 1
    val res1 = solver.countSteps(jumpDistances)
    println("Part 1: number of steps to finish the program: $res1")
    // part 2
    val res2 = solver.countStepsDecreaseDistanceIfGeqThree(jumpDistances)
    println("Part 2: number of steps to finish the program: $res2")
}

fun main() {
    printResult("input.txt")
}
