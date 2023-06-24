package advent_of_code_2015.day21

import Util.readInputLines
import advent_of_code_2015.day21.RPGSimulator.*

/**
 * [RPG Simulator 20XX](https://adventofcode.com/2015/day/21)
 *
 * You're playing a game where you can use weapons to attack the opponent, armor to defend against opponent's attacks,
 * and rings to increase the strength of weapons and/or armor.
 *
 * Each item (weapon, armor, ring) has a cost, each weapon has damage points, each armor has protection points,
 * and each ring adds points to either weapon or armor.
 *
 * You can have exactly one weapon, at most one armor, and at most 2 different rings, from the following items:
 * ```
 * Weapons:    Cost  Damage  Armor
 * Dagger        8     4       0
 * Shortsword   10     5       0
 * Warhammer    25     6       0
 * Longsword    40     7       0
 * Greataxe     74     8       0
 *
 * Armor:      Cost  Damage  Armor
 * Leather      13     0       1
 * Chainmail    31     0       2
 * Splintmail   53     0       3
 * Bandedmail   75     0       4
 * Platemail   102     0       5
 *
 * Rings:      Cost  Damage  Armor
 * Damage +1    25     1       0
 * Damage +2    50     2       0
 * Damage +3   100     3       0
 * Defense +1   20     0       1
 * Defense +2   40     0       2
 * Defense +3   80     0       3
 * ```
 *
 * A player starts with `h` hit points. Each hit of the opponent reduces `h`
 * by the difference of opponent's weapon and player's,
 * yet by at least 1 point even if the armor is stronger than the weapon.
 *
 * Find the minimum cost of items such that the first player wins
 * and the maximum cost of items such that the first player loses.
 */
class RPGSimulator {
    data class EquipmentItem(val name: String, val cost: Int, val damage: Int, val armor: Int)
    data class Player(val hitPoints: Int, val damage: Int, val armor: Int)

    fun findMinCostToWinAndMaxCostLose(hitPoints: Int, opponentPlayer: Player): Pair<Int, Int> {
        var minCostToWin = Int.MAX_VALUE
        var maxCostToLose = -1

        fun checkWinAndCost(w: EquipmentItem, a: EquipmentItem, vararg rings: EquipmentItem) {
            val firstPlayer = Player(hitPoints, damage = calcDamage(w, *rings), armor = calcArmor(a, *rings))
            val cost = w.cost + a.cost + rings.sumOf { it.cost }
            if (firstPlayerWins(firstPlayer, opponentPlayer)) {
                if (minCostToWin > cost) {
                    minCostToWin = cost
                }
            } else {
                if (maxCostToLose < cost) {
                    maxCostToLose = cost
                }
            }
        }

        for (w in weapons) {
            for (a in armors + emptyItem) {
                checkWinAndCost(w, a)
                for (r1 in 0 until rings.size) {
                    checkWinAndCost(w, a, rings[r1])
                    for (r2 in r1 + 1 until rings.size) {
                        checkWinAndCost(w, a, rings[r1], rings[r2])
                    }
                }
            }
        }

        return minCostToWin to maxCostToLose
    }

    private fun calcDamage(weapon: EquipmentItem, vararg rings: EquipmentItem): Int =
        weapon.damage + rings.sumOf { it.damage }

    private fun calcArmor(armor: EquipmentItem, vararg rings: EquipmentItem): Int =
        armor.armor + rings.sumOf { it.armor }

    private fun firstPlayerWins(p1: Player, p2: Player): Boolean {
        val pointLoss1 = (p2.damage - p1.armor).coerceAtLeast(1)
        val pointLoss2 = (p1.damage - p2.armor).coerceAtLeast(1)
        val movesToWin1 = intDivCeiling(p2.hitPoints, pointLoss2)
        val movesToWin2 = intDivCeiling(p1.hitPoints, pointLoss1)
        return movesToWin1 <= movesToWin2
    }

    private fun intDivCeiling(n: Int, d: Int): Int = n / d + if (n % d > 0) 1 else 0

    companion object {
        val emptyItem = EquipmentItem("Empty", 0, 0, 0)

        val weapons = listOf(
            EquipmentItem("Dagger", 8, 4, 0),
            EquipmentItem("Shortsword", 10, 5, 0),
            EquipmentItem("Warhammer", 25, 6, 0),
            EquipmentItem("Longsword", 40, 7, 0),
            EquipmentItem("Greataxe", 74, 8, 0),
        )

        val armors = listOf(
            EquipmentItem("Leather", 13, 0, 1),
            EquipmentItem("Chainmail", 31, 0, 2),
            EquipmentItem("Splintmail", 53, 0, 3),
            EquipmentItem("Bandedmail", 75, 0, 4),
            EquipmentItem("Platemail", 102, 0, 5),
        )

        val rings = listOf(
            EquipmentItem("Damage +1", 25, 1, 0),
            EquipmentItem("Damage +2", 50, 2, 0),
            EquipmentItem("Damage +3", 100, 3, 0),
            EquipmentItem("Defense +1", 20, 0, 1),
            EquipmentItem("Defense +2", 40, 0, 2),
            EquipmentItem("Defense +3", 80, 0, 3),
        )
    }
}


private fun readInput(inputFileName: String): Player {
    val lineMap = readInputLines(2015, 21, inputFileName)
            .map { it.split(": ") }
            .associate { it[0] to it[1].toInt() }
    return Player(
        hitPoints = requireNotNull(lineMap["Hit Points"]),
        damage = requireNotNull(lineMap["Damage"]),
        armor = requireNotNull(lineMap["Armor"])
    )
}

private fun printResult(inputFileName: String) {
    val opponentPlayer = readInput(inputFileName)
    val firstPlayerHitPoints = 100
    val solver = RPGSimulator()
    val (minCostToWin, maxCostToLose) = solver.findMinCostToWinAndMaxCostLose(firstPlayerHitPoints, opponentPlayer)
    println("Minimum cost to win: $minCostToWin")
    println("Maximum cost to win: $maxCostToLose")
}

fun main() {
    printResult("input.txt")
}
