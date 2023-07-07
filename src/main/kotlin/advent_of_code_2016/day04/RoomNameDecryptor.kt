package advent_of_code_2016.day04

import Util.readInputLines

/**
 * [Security Through Obscurity](https://adventofcode.com/2016/day/4)
 *
 * A encrypted room identifier has the form `lowercase letters with dashes-sector_ID-[checksum]`.
 * A room is real (not a decoy), if the checksum is the five most frequent letters,
 * ordered by frequency and alphabetically. For example:
 * - `aaaaa-bbb-z-y-x-123[abxyz]` is a real room because the most common letters are `a` (5), `b` (3),
 * and the letters `x`, `y`, and `z` with frequency 1 are in alphabetical order.
 * - `a-b-c-d-e-f-g-h-987[abcde]` is a real room because from all letters with equal frequency 1,
 * the first five in alphabetical order are listed, in correct order.
 * - `not-a-real-room-404[oarel]` is a real room.
 * - `totally-real-room-200[decoy]` is not a real room.
 *
 * ### Part 1
 *
 * Calculate the sum of the sector IDs of real rooms
 *
 *
 * ### Part 2
 *
 * The room names are encrypted with a [shift cipher](https://en.wikipedia.org/wiki/Caesar_cipher)
 * with a right shift over lowercase letters using the sector ID over, and the dashes become spaces.
 *
 * Decrypt room names and return the sector ID of the room where North Pole objects are stored.
 */
class RoomNameDecryptor {
    data class Room(val name: String, val sectorId: Int, val checksum: String)

    fun sumOfSectorIDsOfValidRooms(roomStrings: List<String>): Int {
        return roomStrings.sumOf { roomString ->
            val room = parseRoom(roomString)
            if (isValidRoom(room)) room.sectorId else 0
        }
    }

    fun findRoomWhereDecryptedNameContains(searchText: String, roomStrings: List<String>): Room? {
        return roomStrings.asSequence()
                .map { parseRoom(it) }
                .filter { isValidRoom(it) }
                .map { decryptRoomName(it) }
                .find { it.name.contains(searchText) }
    }

    private fun parseRoom(roomString: String): Room {
        var i = 0
        while (!roomString[i].isDigit()) i++
        val name = roomString.substring(0..i - 2)

        var j = i + 1
        while (roomString[j].isDigit()) j++
        val sectorId = roomString.substring(i until j).toInt()

        val checksum = roomString.substring(j + 1 until roomString.length - 1)

        return Room(name, sectorId, checksum)
    }

    private fun isValidRoom(room: Room): Boolean {
        val letterCounts = hashMapOf<Char, Int>()
        for (ch in room.name) {
            if (ch == '-') continue
            letterCounts[ch] = (letterCounts[ch] ?: 0) + 1
        }
        val expectedChecksum = letterCounts.toList()
                .sortedWith(compareByDescending<Pair<Char, Int>> { (_, count) -> count }.thenBy { (char, _) -> char })
                .take(5)
                .joinToString(separator = "") { (char, _) -> char.toString() }

        return room.checksum == expectedChecksum
    }

    private fun decryptRoomName(room: Room): Room {
        val shift = room.sectorId % 26

        val decryptedName = StringBuilder().apply {
            for (ch in room.name) append(
                if (ch == '-') {
                    ' '
                } else {
                    var chShifted = ch + shift
                    if (chShifted > 'z') {
                        chShifted -= 26
                    }
                    chShifted
                }
            )
        }.toString()

        return room.copy(name = decryptedName)
    }
}


private fun readInput(inputFileName: String): List<String> =
    readInputLines(2016, 4, inputFileName)

private fun printResult(inputFileName: String) {
    val roomStrings = readInput(inputFileName)
    val solver = RoomNameDecryptor()
    val res1 = solver.sumOfSectorIDsOfValidRooms(roomStrings)
    println("Sum of sector IDs of valid rooms: $res1")

    val res2 = solver.findRoomWhereDecryptedNameContains("northpole", roomStrings)
    println("Room with decrypted name containing 'northpole': $res2")
}

fun main() {
    printResult("input.txt")
}