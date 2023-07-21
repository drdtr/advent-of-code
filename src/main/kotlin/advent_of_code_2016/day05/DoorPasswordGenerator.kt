package advent_of_code_2016.day05

import Util.md5
import Util.readInputLines

/**
 * [How About a Nice Game of Chess](https://adventofcode.com/2016/day/5)
 *
 * Generade the password to a security door for the door ID.
 *
 * ### Part 1
 * The password has 8 characters and is produced as follows:
 * 1. start with the Door ID  and an integer index 0
 * 2. increment the index until the string consisting of the Door ID and the index
 * yields an [MD5](https://en.wikipedia.org/wiki/MD5) hash starting with five zeroes.
 * Pick the 6th character of the MD5 hash - this is the next password character.
 * 3. repeat previous step until obtaining all 8 characters of the password
 *
 * Example:
 * - For the Door ID `abc`, the first index that produces a hash starting with 5 zeroes is 3231929,
 * i.e. `abc3231929`. The sixth character of the hash is `1`.
 * - The next index is `5017308`, for which the hash of `abc5017308` starts with `000008f82` and hence produces
 * the `8` as the next character of the password.
 * - This way, the produced password is `18f47a30`.
 *
 *
 * ### Part 2
 *
 * Now, the password characters are not generated in order - instead,
 * the 6th character of a suitable MD5 hash indicates the position `0..7` in the password
 * and the 7th character is the password character.
 * Hashes with invalid positions, i.e. outside of `0..7`, are skipped.
 *
 * Example:
 * - For the Door ID `abc`, the first index that produces a suitable hash is 3231929:
 * the has is `0000015...`, so the position `1` of the password is `5`.
 * - The index `5017308`, other than in Part 1, is not suitable
 * because the produces MD5 hash specifies an invalid position 8.
 * - The second suitable has is at index `5357525`, which produces `000004e`
 * so that the 4th character in the password is `e`.
 * - This way, the produced password is `05ace8e3`.
 */
class DoorPasswordGenerator {
    fun generateDoorPassword(doorId: String, targetLength: Int = 8): String = buildString {
        for (i in 0 until Int.MAX_VALUE) {
            val s = doorId + i
            val hash = s.md5()
            if (hash.startsWith("00000")) {
                append(hash[5])
                if (length == targetLength) break
            }
        }
    }

    fun generateDoorPasswordWithPosition(doorId: String, targetLength: Int = 8): String {
        val empty = ' '
        val chars = CharArray(targetLength) { empty }
        var filledCharsCount = 0

        fun isValidPosChar(posChar: Char) = posChar in '0' until '0' + targetLength

        for (i in 0 until Int.MAX_VALUE) {
            val s = doorId + i
            val hash = s.md5()
            if (hash.startsWith("00000")) {
                if (!isValidPosChar(hash[5])) continue
                val pos = hash[5] - '0'
                if (chars[pos] == empty) {
                    filledCharsCount++
                    chars[pos] = hash[6]
                }
                if (filledCharsCount == targetLength) break
            }
        }

        return String(chars)
    }
}

private fun readInput(inputFileName: String): String =
    readInputLines(2016, 5, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val roomId = readInput(inputFileName)
    val solver = DoorPasswordGenerator()
    val res1 = solver.generateDoorPassword(roomId, 8)
    println("Door password, generated sequentially: $res1")
    val res2 = solver.generateDoorPasswordWithPosition(roomId, 8)
    println("Door password, generated with character position: $res2")
}

fun main() {
    printResult("input.txt")
}