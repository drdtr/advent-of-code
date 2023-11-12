package advent_of_code_2016.day19

import Util.readInputLines

/**
 * [An Elephant Named Joseph](https://adventofcode.com/2016/day/19)
 *
 * ### Part 1
 *
 * `n` players participate in a cyclic elimination game, where the player in each turn eliminates the next uneliminated
 * player (cyclic meaning that in the beginning after player `n - 1` comes player `0`).
 *
 * Find out, which player will remain uneliminated.
 */
class CyclicElimination {
    fun lastUneliminated(n: Int): Int {
        val nextPlayers = IntArray(n) { it + 1 }
        nextPlayers[n - 1] = 0
        var remainingPlayers = n
        var i = 0
        while (remainingPlayers > 1) {
            // eliminate next player
            val nextIdx = nextPlayers[i]
            val nextNextIdx = nextPlayers[nextIdx]
            nextPlayers[i] = nextNextIdx
            // we could mark the removed player e.g. by -1, but this is not needed because
            // the removed player won't be visited again so the value won't be used
            i = nextNextIdx
            remainingPlayers--
        }
        return i
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2016, 19, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val n = readInput(inputFileName)
    val solver = CyclicElimination()
    // part 1
    val res1 = solver.lastUneliminated(n)
    println("The number of the last remaining player 0-based is $res1 and 1-base is ${res1 + 1}")
}

fun main() {
    printResult("input.txt")
}
