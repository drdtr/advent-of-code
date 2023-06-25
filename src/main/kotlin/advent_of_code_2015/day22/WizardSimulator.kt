package advent_of_code_2015.day22

import Util.readInputLines

/**
 * [Wizard Simulator 20XX](https://adventofcode.com/2015/day/22)
 *
 * Similar to [RPG Simulator 20XX](https://adventofcode.com/2015/day/21), you're playing a game,
 * however this time you use magic spells, that cost _mana_:
 *
 * |Spell          | Cost | Damage |  Heal | Armor | Mana | Duration |
 * |---------------|------|--------|-------|-------|------|----------|
 * |Magic Missile  | 53   | 4      | -     | -     | -    | -        |
 * |Drain          | 73   | 2      | 2     | -     | -    | -        |
 * |Shield         | 113  | -      | -     | 7     | -    | 6        |
 * |Poison         | 173  | 3      | -     | -     | -    | 6        |
 * |Recharge       | 229  | -      | -     | -     | 101  | 5        |
 *
 *
 * Starting with 50 hit points and 500 mana points, what is the least amount of mana you need to spend to win.
 *
 *
 * Note: used the idea to terminate a simulated game run when spent mana reaches currently recorded minimum cost,
 * as seen [here](https://github.com/narimiran/advent_of_code_2015/blob/master/python/day22.py#L42)
 * in a slightly different form, because this run would not improve the minimum cost -
 * this greatly reduces runtime because it avoids long unnecessary runs that would only result
 * in higher cost than already found minimum cost.
 */
class WizardSimulator {
    enum class Spell(
        val cost: Int,
        val damage: Int = 0,
        val armor: Int = 0,
        val heal: Int = 0,
        val mana: Int = 0,
        val duration: Int? = null
    ) {
        MagicMissile(cost = 53, damage = 4),
        Drain(cost = 73, damage = 2, heal = 2),
        Shield(cost = 113, armor = 7, duration = 6),
        Poison(cost = 173, damage = 3, duration = 6),
        Recharge(cost = 229, mana = 101, duration = 5),
    }

    fun findMinCostToWin(
        hitPoints: Int,
        manaPoints: Int,
        opponentHitPoints: Int,
        opponentDamage: Int,
        hardMode: Boolean = false,
    ): Int =
        MinCostFinder(hitPoints, manaPoints, opponentHitPoints, opponentDamage, hardMode).findMinCost()

    private class MinCostFinder(
        val hitPoints: Int,
        val startingManaPoints: Int,
        val opponentHitPoints: Int,
        val opponentDamage: Int,
        val isHardMode: Boolean,
    ) {
        private data class SimulationState(
            var opponentHitPoints: Int,
            var hitPoints: Int,
            var manaPoints: Int,
            var manaSpent: Int = 0,
            var remainingSpellDurations: MutableMap<Spell, Int> = mutableMapOf(),
            var isPlayerTurn: Boolean = true,
            var armorForThisTurn: Int = 0,
        ) {
            fun deepCopy() = copy().apply { remainingSpellDurations = remainingSpellDurations.toMutableMap() }
        }

        private var minCost = Int.MAX_VALUE

        fun findMinCost(): Int {
            SimulationState(opponentHitPoints, hitPoints, startingManaPoints).simulate()
            return minCost
        }

        private fun SimulationState.simulate() {
            if (isPlayerTurn && isHardMode) {
                hitPoints--
            }

            // check if any of the players has already won
            if (checkGameFinishedAndRecordMinCost()) {
                return
            }

            armorForThisTurn = 0 // reset before applying effects of spells with timer
            // apply effects of spells with timer
            for ((spell, remainingDuration) in remainingSpellDurations.toMap()) {
                applyEffect(spell, remainingDuration)
            }
            // check if any of the players has won after effects take place
            if (checkGameFinishedAndRecordMinCost()) {
                return
            }

            if (isPlayerTurn) {
                val possibleSpells = Spell.values().asSequence()
                        .filterNot { it in remainingSpellDurations }
                        .filterNot { it.cost > manaPoints }
                        .toList()
                if (possibleSpells.isEmpty()) {
                    return // player loses because can't cast any spell
                }
                for (spell in possibleSpells) {
                    with(deepCopy()) {
                        isPlayerTurn = false
                        castSpell(spell)
                        simulate()
                    }
                }
            } else {
                // it's the turn of the opponent
                with(deepCopy()) {
                    isPlayerTurn = true
                    hitPoints -= (opponentDamage - armorForThisTurn).coerceAtLeast(1)
                    simulate()
                }
            }
        }

        fun SimulationState.checkGameFinishedAndRecordMinCost(): Boolean {
            if (hitPoints <= 0) {
                // player loses
                return true
            }
            if (opponentHitPoints <= 0) {
                // player wins
                if (minCost > manaSpent) {
                    minCost = manaSpent
                }
                return true
            }
            if (manaSpent >= minCost) {
                // can't improve the result
                return true
            }
            return false
        }


        private fun SimulationState.applyEffect(spell: Spell, remainingDuration: Int) {
            applySpell(spell)

            if (remainingDuration > 1)
                remainingSpellDurations[spell] = remainingDuration - 1
            else
                remainingSpellDurations -= spell
        }

        private fun SimulationState.castSpell(spell: Spell) {
            manaPoints -= spell.cost
            manaSpent += spell.cost
            if (spell.duration == null) {
                applySpell(spell)
            } else {
                remainingSpellDurations[spell] = spell.duration
            }
        }

        private fun SimulationState.applySpell(spell: Spell) {
            opponentHitPoints -= spell.damage
            armorForThisTurn += spell.armor
            hitPoints += spell.heal
            manaPoints += spell.mana
        }
    }
}

private data class Input(val hitPoints: Int, val damage: Int)

private fun readInput(inputFileName: String): Input {
    val lineMap = readInputLines(2015, 22, inputFileName)
            .map { it.split(": ") }
            .associate { it[0] to it[1].toInt() }
    return Input(
        hitPoints = requireNotNull(lineMap["Hit Points"]),
        damage = requireNotNull(lineMap["Damage"]),
    )
}

private fun printResult(inputFileName: String) {
    val opponent = readInput(inputFileName)
    val hitPoints = 50
    val manaPoints = 500
    val solver = WizardSimulator()
    val minCostToWin = solver.findMinCostToWin(hitPoints, manaPoints, opponent.hitPoints, opponent.damage)
    println("Minimum cost to win: $minCostToWin")
    val minCostToWinInHardMode =
        solver.findMinCostToWin(hitPoints, manaPoints, opponent.hitPoints, opponent.damage, hardMode = true)
    println("Minimum cost to win in hard mode: $minCostToWinInHardMode")
}

fun main() {
    printResult("input.txt")
}
