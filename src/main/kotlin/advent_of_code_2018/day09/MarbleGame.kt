package advent_of_code_2018.day09

import Util.readInputLines

/**
 * [Marble Mania](https://adventofcode.com/2018/day/9)
 *
 * `k` players play a marble game with following rules:
 * - After each move, the placed marble becomes the _current_ one.
 * - First, the marble 0 is placed in the circle.
 * - The 1st player places marble 1.
 * - In each move i >= 2, the player places the marble between 1st and 2nd marble after the current marble
 * in clockwise direction.
 * - If the marble value is a multiple of 23, then the current player keeps it and adds its value to their score.
 * In addition, the marble 7 positions counter-clockwise from the current marble is removed and its value also added
 * to the player's score. The marble clockwise after the removed one becomes the new current marble.
 * - The winner is the player with the highest score after the last marble is used up.
 *
 * Given the number of players `k` and the max. marble value `n`, find the winning score.
 *
 * Example with 9 players
 * ```
 * [-] (0)
 * [1]  0 (1)
 * [2]  0 (2) 1
 * [3]  0  2  1 (3)
 * [4]  0 (4) 2  1  3
 * [5]  0  4  2 (5) 1  3
 * [6]  0  4  2  5  1 (6) 3
 * [7]  0  4  2  5  1  6  3 (7)
 * [8]  0 (8) 4  2  5  1  6  3  7
 * [9]  0  8  4 (9) 2  5  1  6  3  7
 * [1]  0  8  4  9  2(10) 5  1  6  3  7
 * [2]  0  8  4  9  2 10  5(11) 1  6  3  7
 * [3]  0  8  4  9  2 10  5 11  1(12) 6  3  7
 * [4]  0  8  4  9  2 10  5 11  1 12  6(13) 3  7
 * [5]  0  8  4  9  2 10  5 11  1 12  6 13  3(14) 7
 * [6]  0  8  4  9  2 10  5 11  1 12  6 13  3 14  7(15)
 * [7]  0(16) 8  4  9  2 10  5 11  1 12  6 13  3 14  7 15
 * [8]  0 16  8(17) 4  9  2 10  5 11  1 12  6 13  3 14  7 15
 * [9]  0 16  8 17  4(18) 9  2 10  5 11  1 12  6 13  3 14  7 15
 * [1]  0 16  8 17  4 18  9(19) 2 10  5 11  1 12  6 13  3 14  7 15
 * [2]  0 16  8 17  4 18  9 19  2(20)10  5 11  1 12  6 13  3 14  7 15
 * [3]  0 16  8 17  4 18  9 19  2 20 10(21) 5 11  1 12  6 13  3 14  7 15
 * [4]  0 16  8 17  4 18  9 19  2 20 10 21  5(22)11  1 12  6 13  3 14  7 15
 * [5]  0 16  8 17  4 18(19) 2 20 10 21  5 22 11  1 12  6 13  3 14  7 15
 * [6]  0 16  8 17  4 18 19  2(24)20 10 21  5 22 11  1 12  6 13  3 14  7 15
 * [7]  0 16  8 17  4 18 19  2 24 20(25)10 21  5 22 11  1 12  6 13  3 14  7 15
 * ```
 * In the example above with 9 players and the last marble having the value 25,
 * the winner is player 5 who kept the marble 23 and removed the marble 9
 * so that the winning score was 23 + 9 = 32.
 *
 * More examples:
 * - 10 players; last marble is worth 1618 points: high score is 8317
 * - 13 players; last marble is worth 7999 points: high score is 146373
 * - 17 players; last marble is worth 1104 points: high score is 2764
 * - 21 players; last marble is worth 6111 points: high score is 54718
 * - 30 players; last marble is worth 5807 points: high score is 37305
 *
 * ### Part 1
 *
 * What is the winning score for the given input?
 *
 * ### Part 2
 *
 * What is the winning score for the given input, after multiplying the max. marble value by 100?
 *
 */
class MarbleGame {
    fun calcMarbleGameWinningScore(numOfPlayers: Int, maxMarbleValue: Int): Long =
        with(MarbleGameEmulator(numOfPlayers, maxMarbleValue)) {
            emulate()
            getMaxPlayerScore()
        }

    private class MarbleGameEmulator(val numOfPlayers: Int, val maxMarbleValue: Int) {
        private val placedMarbles = CircularNonEmptyLinkedList(0)
        private val playerScores = LongArray(numOfPlayers)


        fun emulate() {
            var player = 0
            for (marble in 1..maxMarbleValue) {
                makeMove(player, marble)
                player = (player + 1) % numOfPlayers
            }
        }

        private fun makeMove(player: Int, marble: Int) {
            if (marble % 23 == 0) {
                repeat(7) { placedMarbles.prev() }
                playerScores[player] += (marble + placedMarbles.get()).toLong()
                placedMarbles.remove()
            } else {
                placedMarbles.next()
                placedMarbles.add(marble)
            }
        }

        fun getMaxPlayerScore(): Long = playerScores.max()
    }
}

private data class PlayersAndMarblesInput(val numOfPlayers: Int, val maxMarbleValue: Int)

private fun readInput(inputFileName: String): PlayersAndMarblesInput =
    readInputLines(2018, 9, inputFileName)[0].split(" ").let { subStrings ->
        PlayersAndMarblesInput(numOfPlayers = subStrings[0].toInt(), maxMarbleValue = subStrings[6].toInt())
    }

private fun printResult(inputFileName: String) {
    val playersAndMarblesInput = readInput(inputFileName)
    println(playersAndMarblesInput)
    val solver = MarbleGame()

    with(playersAndMarblesInput) {
        val res1 = solver.calcMarbleGameWinningScore(numOfPlayers, maxMarbleValue)
        println("Winning player's score for $numOfPlayers players and max. marble value $maxMarbleValue: $res1")

        val maxMarbleValue2 = maxMarbleValue * 100
        val res2 = solver.calcMarbleGameWinningScore(numOfPlayers, maxMarbleValue2)
        println("Winning player's score for $numOfPlayers players and max. marble value $maxMarbleValue2: $res2")
    }
}

fun main() {
    printResult("input.txt")
}


