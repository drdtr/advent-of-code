package advent_of_code_2016.day10

import Util.readInputLines
import advent_of_code_2016.day10.BotSimulator.*
import java.util.ArrayDeque
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.min

/**
 * [Balance Bots](https://adventofcode.com/2016/day/10)
 *
 * You're given a list of instructions that can have the following form:
 * - `value v goes to bot x`
 * - `bot x gives low to bot y and high to bot z`
 * - `bot x gives low to output a and high to bot z`
 * - `bot x gives low to output a and high to output b`
 *
 * **Example**:
 * - `value 5 goes to bot 2`
 * - `bot 2 gives low to bot 1 and high to bot 0`
 * - `value 3 goes to bot 1`
 * - `bot 1 gives low to output 1 and high to bot 0`
 * - `bot 0 gives low to output 2 and high to output 0`
 *
 * In the end, the output 0 has value 5, output 1 has value 2, and output 2 has value 3.
 * Also, Bot 2 was responsible for comparing 5 and 2.
 *
 * Find out the number of the bot that was responsible for comparing 61 and 17.
 */
class BotSimulator {
    data class BotState(val valuesQueue: ArrayDeque<Int> = ArrayDeque())
    data class OutputState(var value: Int = 0)
    data class Comparison(val low: Int, val high: Int)
    data class SimulationState(
        /** Map of bot numbers to bot states */
        val bots: MutableMap<Int, BotState> = hashMapOf(),
        /** Map of output numbers to output states */
        val outputs: MutableMap<Int, OutputState> = hashMapOf(),
        /** Map of bot numbers to the comparisons they performed  */
        val botComparisons: MutableMap<Int, MutableSet<Comparison>> = hashMapOf(),
        /** Map of bot numbers to blocked send operations awaiting an input to the bot */
        val botAwaitingSendOpsStack: MutableMap<Int, ArrayDeque<BotSendOp>> = hashMapOf(),
    )

    sealed interface BotOp
    data class InputOp(val value: Int, val botNumber: Int) : BotOp
    data class BotSendOp(val botNumber: Int, val lowSendTarget: SendTarget, val highSendTarget: SendTarget) : BotOp

    sealed interface SendTarget
    data class BotTarget(val botNumber: Int) : SendTarget
    data class OutputTarget(val outputNumber: Int) : SendTarget

    fun simulate(ops: Iterable<BotOp>): SimulationState = with(Simulator()) {
        simulate(ops)
        return simulationState
    }

    fun findBotNumberThatPerformedComparison(simulationState: SimulationState, comparison: Comparison): Int? =
        simulationState.botComparisons.entries.find { (_, comparisons) -> comparison in comparisons }?.key

    private class Simulator {
        val simulationState = SimulationState()

        fun simulate(ops: Iterable<BotOp>) {
            for (op in ops) when (op) {
                is InputOp   -> {
                    val bot = getBot(op.botNumber)
                    bot.valuesQueue.offer(op.value)
                    performAwaitingSendIfUnblocked(op.botNumber)
                }

                is BotSendOp -> {
                    val bot = getBot(op.botNumber)
                    if (bot.valuesQueue.size >= 2) {
                        performSendOp(op)
                    } else {
                        getAwaitingSendOps(op.botNumber).push(op)
                    }
                }
            }
        }

        private fun getBot(botNumber: Int) = simulationState.bots.getOrPut(botNumber) { BotState() }
        private fun getOutput(outputNumber: Int) = simulationState.outputs.getOrPut(outputNumber) { OutputState() }
        private fun getComparisons(botNumber: Int) =
            simulationState.botComparisons.getOrPut(botNumber) { hashSetOf() }

        private fun getAwaitingSendOps(botNumber: Int) =
            simulationState.botAwaitingSendOpsStack.getOrPut(botNumber) { ArrayDeque() }

        private fun send(value: Int, sendTarget: SendTarget) {
            when (sendTarget) {
                is BotTarget    -> getBot(sendTarget.botNumber).valuesQueue.offer(value)
                is OutputTarget -> getOutput(sendTarget.outputNumber).value = value
            }
        }

        private fun performSendOp(op: BotSendOp) {
            val bot = getBot(op.botNumber)
            val a = bot.valuesQueue.poll()
            val b = bot.valuesQueue.poll()
            val low = min(a, b)
            val high = max(a, b)
            send(low, op.lowSendTarget)
            send(high, op.highSendTarget)
            getComparisons(op.botNumber).add(Comparison(low, high))
            if (op.lowSendTarget is BotTarget) performAwaitingSendIfUnblocked(op.lowSendTarget.botNumber)
            if (op.highSendTarget is BotTarget) performAwaitingSendIfUnblocked(op.highSendTarget.botNumber)
        }

        private fun performAwaitingSendIfUnblocked(botNumber: Int) {
            val bot = getBot(botNumber)
            if (bot.valuesQueue.size >= 2) {
                with(getAwaitingSendOps(botNumber)) {
                    if (isNotEmpty()) performSendOp(pop())
                }
            }
        }
    }
}

private object BotOpInputPatterns {
    val input = Pattern.compile("value (\\d+) goes to bot (\\d+)")
    val send = Pattern.compile("bot (\\d+) gives low to (\\w+) (\\d+) and high to (\\w+) (\\d+)")
}

private fun parseBotOp(s: String): BotOp {
    with(BotOpInputPatterns.input.matcher(s)) {
        if (matches()) return InputOp(value = group(1).toInt(), botNumber = group(2).toInt())
    }
    with(BotOpInputPatterns.send.matcher(s)) {
        if (matches()) return BotSendOp(
            botNumber = group(1).toInt(),
            lowSendTarget = parseSendTarget(group(2), group(3).toInt()),
            highSendTarget = parseSendTarget(group(4), group(5).toInt()),
        )
    }
    error("Invalid bot operation $s")
}

private fun parseSendTarget(target: String, targetNumber: Int): SendTarget = when (target) {
    "output" -> OutputTarget(outputNumber = targetNumber)
    "bot"    -> BotTarget(botNumber = targetNumber)
    else     -> error("Invalid bot send target $target")
}

private fun readInput(inputFileName: String): List<BotOp> =
    readInputLines(2016, 10, inputFileName).map { parseBotOp(it) }

private fun printSimulationState(simulationState: SimulationState) = with(simulationState) {
    println(
        """Simulation result: 
        |bots: ${bots}
        |outputs: ${outputs} 
        |botComparisons: ${botComparisons}
        |botAwaitingSendOps: ${botAwaitingSendOpsStack}""".trimMargin()
    )
}

private fun printResult(inputFileName: String) {
    val botOps = readInput(inputFileName)
    val solver = BotSimulator()
    val simulationState = solver.simulate(botOps)
//    printSimulationState(simulationState)

    //  Part 1
    val comparisonToFind = Comparison(low = 17, high = 61)
    val botWithComparison = solver.findBotNumberThatPerformedComparison(simulationState, comparisonToFind)
    requireNotNull(botWithComparison) { "Couldn't find a bot with comparison $comparisonToFind" }
    println("Bot with $comparisonToFind: $botWithComparison")

    // Part 2
    val outputNumbers = listOf(0, 1, 2)
    val outputValues = outputNumbers.map { i -> simulationState.outputs[i]!!.value }
    val product = outputValues.fold(1) { a, b -> a * b }
    println("Product of outputs $outputNumbers: $product")
}

fun main() {
    printResult("input.txt")
}