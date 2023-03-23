package advent_of_code_2015.day13

import Util.readInputLines
import advent_of_code_2015.day13.OptimalSeating.*

/**
 * [Knights of the Dinner Table](https://adventofcode.com/2015/day/13)
 *
 * A group of guests must be seated at a round table that has exactly the number of seats to fit everyone.
 * For each guest, a number of happiness units is given, which the guest would gain/lose when sitting
 * next to a given other guest. For example:
 * - `Alice` would gain 54 happiness units by sitting next to `Bob`.
 * - `Alice` would lose 79 happiness units by sitting next to `Carol`.
 * - `Bob` would gain 83 happiness units by sitting next to `Alice`.
 * - `Bob` would lose 7 happiness units by sitting next to `Carol`.
 *
 * Find the maximum possible sum of happiness units achievable by an optimal seating.
 */
class OptimalSeating {
    data class HappinessChange(val guest: String, val neighbor: String, val happinessChange: Int)

    fun maximumHappiness(happinessChanges: Collection<HappinessChange>): Int =
        if (happinessChanges.size == 2)
            happinessChanges.sumOf { it.happinessChange }
        else
            HappinessMaximizer(happinessChanges).findMaxHappiness()

    private class HappinessMaximizer(happinessChanges: Collection<HappinessChange>) {
        private val happinessChangesMap = happinessChanges
                .groupBy { it.guest }
                .map { (guest, happinessChanges) -> guest to happinessChanges.associate { it.neighbor to it.happinessChange } }
                .toMap()

        private val guests = happinessChangesMap.keys
        private val n = guests.size
        private val isSeatedGuest = guests.map { it to false }.toMap(mutableMapOf())
        private val seatingOrder = Array(guests.size) { "" }

        private var maxHappiness = Int.MIN_VALUE

        fun findMaxHappiness(): Int {
            // the table is round, so we can pick any guest to seat at position `0`
            val firstGuest = guests.first()
            isSeatedGuest[firstGuest] = true
            seatingOrder[0] = firstGuest

            backtrack(numSeated = 1, totalHappinessSoFar = 0)

            return maxHappiness
        }

        private fun getHappiness(guest: String, neighbor: String): Int =
            happinessChangesMap[guest]?.get(neighbor)
                ?: error("Happiness entry missing for $guest for neighbor $neighbor")

        private fun getHappinessBidirectional(guest1: String, guest2: String): Int =
            getHappiness(guest1, guest2) + getHappiness(guest2, guest1)

        private fun backtrack(numSeated: Int, totalHappinessSoFar: Int) {
            if (numSeated == n) {
                val lastWithFirstHappiness = getHappinessBidirectional(seatingOrder[n - 1], seatingOrder[0])
                val totalHappiness = totalHappinessSoFar + lastWithFirstHappiness
                if (maxHappiness < totalHappiness) maxHappiness = totalHappiness
                return
            }

            for (guest in guests) {
                if (isSeatedGuest[guest] == true) continue

                isSeatedGuest[guest] = true
                seatingOrder[numSeated] = guest

                val previousWithCurrentHappiness = getHappinessBidirectional(seatingOrder[numSeated - 1], guest)
                backtrack(numSeated + 1, totalHappinessSoFar + previousWithCurrentHappiness)

                isSeatedGuest[guest] = false
            }
        }
    }
}

private fun readInput(inputFileName: String): List<HappinessChange> {
    val lines = readInputLines(2015, 13, inputFileName)
    return lines.asSequence()
            .map { it.split(" ", ".") }
            .map { HappinessChange(it[0], it[10], it[3].toInt() * (if (it[2] == "lose") -1 else 1)) }
            .toList()
}

private fun printResult(inputFileName: String) {
    val happinessChanges = readInput(inputFileName)
    val solver = OptimalSeating()
    println("Maximum happiness: ${solver.maximumHappiness(happinessChanges)}")

    val happinessChangesWithHost = addHostIndifferent(happinessChanges)
    println("Maximum happiness with host: ${solver.maximumHappiness(happinessChangesWithHost)}")
}

private fun addHostIndifferent(happinessChanges: Collection<HappinessChange>): Collection<HappinessChange> {
    val guests = happinessChanges.mapTo(hashSetOf()) { it.guest }
    val host = ""
    val guestsWithHostIndifferent =
        guests.map { HappinessChange(it, host, 0) } + guests.map { HappinessChange(host, it, 0) }
    return happinessChanges + guestsWithHostIndifferent
}

fun main() {
    printResult("input.txt")
}