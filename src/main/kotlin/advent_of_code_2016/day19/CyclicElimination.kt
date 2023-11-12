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
 *
 * ### Part 2
 *
 * Now, the player across the circle is eliminated and when there are two, then the left one.
 *
 * Solution idea from reddit forum[here](https://www.reddit.com/r/adventofcode/comments/5j4lp1/comment/dbdf7xr/) and
 * [here](https://www.reddit.com/r/adventofcode/comments/5j4lp1/comment/dbdf9mn/)
 * - Split the players into two equally sized queues
 * - In each turn we remove one player
 *    - if the number of players is even then remove the last player from the first queue
 *    - if the number of players is odd then remove the first player from the second queue
 * - then we put the current player into the 2nd queue and shift the first player from the 2nd queue to the 1st queue
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

    fun lastUneliminatedAcrossCircle(n: Int): Int {
        if (n <= 1) return 0

        val q1 = java.util.ArrayDeque<Int>().apply { addAll(0 until n / 2) }
        val q2 = java.util.ArrayDeque<Int>().apply { addAll(n / 2 until n) }

        while (q1.size + q2.size > 1) {
            val currPlayer = q1.poll()
            if (q1.size == q2.size) {
                q1.pollLast()
            } else {
                q2.pollFirst()
            }
            q2.add(currPlayer)
            q1.add(q2.pollFirst())

        }
        return q1.poll()
    }
}

private fun readInput(inputFileName: String): Int =
    readInputLines(2016, 19, inputFileName)[0].toInt()

private fun printResult(inputFileName: String) {
    val n = readInput(inputFileName)
    val solver = CyclicElimination()
    // part 1
    val res1 = solver.lastUneliminated(n)
    println("The number of the last remaining player is 0-based $res1 and 1-base ${res1 + 1}")
    // part 2
    val res2 = solver.lastUneliminatedAcrossCircle(n)
    println(
        "The number of the last remaining player, when eliminating across circle, " +
                "is 0-based $res2 and 1-base ${res2 + 1}"
    )
}

fun main() {
    printResult("input.txt")
}
