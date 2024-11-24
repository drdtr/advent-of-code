package advent_of_code_2017.day25

import Util.readInputLines
import advent_of_code_2017.day25.TuringMachine.*
import advent_of_code_2017.day25.TuringMachine.CursorStep.*
import advent_of_code_2017.day25.TuringMachineEmulationInputParser.*
import java.util.regex.Pattern

/**
 * [The Halting Problem](https://adventofcode.com/2017/day/25)
 *
 * Emulate a Turing machine consisting of:
 * - an infinite tape where each cell can be 0 or 1
 * - a cursor that can move left or right
 * - a set of states
 *
 * After running the emulation for `n`, calculate the `checksum` defined as the number of `1` appearing on the tape.
 */
class TuringMachineEmulator {
    class TuringMachineTape<T> {
        private val cellValues: MutableMap<Int, T> = hashMapOf()
        var leftMostIndex = 0
            private set
        var rightMostIndex = 0
            private set

        operator fun set(key: Int, value: T) {
            cellValues.set(key, value)
            if (key < leftMostIndex) leftMostIndex = key
            else if (key > rightMostIndex) rightMostIndex = key
        }

        operator fun get(key: Int) = cellValues[key]
    }

    data class TuringMachineEmulationState(
        var state: State,
        var cursorPos: Int = 0,
        var turingMachineTape: TuringMachineTape<Int> = TuringMachineTape(),
    )

    fun emulateAndGetChecksum(turingMachine: TuringMachine, initState: State, numSteps: Int): Int =
        with(emulate(turingMachine, initState, numSteps).turingMachineTape) {
            (leftMostIndex..rightMostIndex).sumOf { get(it) ?: 0 }
        }

    private fun emulate(turingMachine: TuringMachine, initState: State, numSteps: Int): TuringMachineEmulationState {
        val transitionsByStateAndCellValue: Map<State, Map<Int, List<Transition>>> =
            turingMachine.possibleTransitions
                    .groupBy { it.state }
                    .mapValues { (_, transitions) -> transitions.groupBy { it.cellValue } }

        return TuringMachineEmulationState(initState).apply {
            repeat(numSteps) {
                val transitionsByCellValue = requireNotNull(transitionsByStateAndCellValue[state]) {
                    "No transition found for state $state"
                }
                val cellValue = turingMachineTape[cursorPos] ?: 0
                val transition = requireNotNull(transitionsByCellValue[cellValue]?.firstOrNull()) {
                    "No transition found for state $state and cell value $cellValue"
                }
                applyTransition(transition)
            }
        }
    }

    private fun TuringMachineEmulationState.applyTransition(transition: Transition) {
        state = transition.newState
        turingMachineTape[cursorPos] = transition.newCellValue
        when (transition.cursorStep) {
            Left  -> cursorPos--
            Right -> cursorPos++
        }
    }
}

data class TuringMachine(val possibleTransitions: List<Transition>) {
    enum class CursorStep { Left, Right }
    data class State(val s: String)
    data class Transition(
        val state: State,
        val cellValue: Int,
        val newState: State,
        val newCellValue: Int,
        val cursorStep: CursorStep,
    )

    override fun toString() =
        "TuringMachine(possibleTransitions=" +
                possibleTransitions.joinToString(prefix = "[\n  ", separator = "\n  ", postfix = "]")
}

private class TuringMachineEmulationInputParser(val lines: List<String>) {
    data class TuringMachineEmulationInput(val turingMachine: TuringMachine, val initState: State, val numOfSteps: Int)

    private val initStatePattern = Pattern.compile("Begin in state (\\w+).*")
    private val checksumStepsPattern = Pattern.compile("Perform a diagnostic checksum after (\\d+).*")
    private val ifInStatePattern = Pattern.compile("In state (\\w+).*")
    private val ifCurrentValuePattern = Pattern.compile(".*If the current value is (\\d+).*")
    private val writeValuePattern = Pattern.compile(".*Write the value (\\d+).*")
    private val moveCursorPattern = Pattern.compile(".*Move one slot to the (right|left).*")
    private val newStatePattern = Pattern.compile(".*Continue with state (\\w+).*")

    private var lineIndex = 0


    fun parseTuringMachineEmulationInput(): TuringMachineEmulationInput {
        val initState = parseInitState(lines[lineIndex++])
        val numOfSteps = parseNumOfStepsForChecksum(lines[lineIndex++])
        val transitions = mutableListOf<Transition>()

        while (lineIndex < lines.size) {
            if (lines[lineIndex].isBlank())
                lineIndex++
            else
                transitions += parseTransitionsFromState()
        }

        return TuringMachineEmulationInput(TuringMachine(transitions), initState, numOfSteps)
    }

    private fun parseInitState(s: String): State = with(initStatePattern.matcher(s)) {
        require(matches()) { "Line 1 must define the initial state, but was $s" }
        State(group(1).trim())
    }

    private fun parseNumOfStepsForChecksum(s: String): Int = with(checksumStepsPattern.matcher(s)) {
        require(matches()) { "Line 2 must indicate the number of steps to perform, but was $s" }
        group(1).toInt()
    }

    private fun parseTransitionsFromState(): List<Transition> = buildList {
        val s = lines[lineIndex++]
        val state: State
        with(ifInStatePattern.matcher(s)) {
            require(matches()) { "Line ${lineIndex - 1} must define the source state of transition, but was $s" }
            state = State(group(1).trim())
        }
        do {
            val transition = parseTransitionFromStateAndValue(state)
            transition?.let { add(it) }
        } while (transition != null)
    }

    private fun parseTransitionFromStateAndValue(state: State): Transition? {
        if (lineIndex >= lines.size) return null

        var s = lines[lineIndex++]
        val cellValue = with(ifCurrentValuePattern.matcher(s)) {
            if (!matches()) return null
            group(1).toInt()
        }
        s = lines[lineIndex++]
        val newCellValue = with(writeValuePattern.matcher(s)) {
            require(matches()) { "Line must define next cell value, but was $s" }
            group(1).toInt()
        }
        s = lines[lineIndex++]
        val nextCursorStep = with(moveCursorPattern.matcher(s)) {
            require(matches()) { "Line must define next cursor step, but was $s" }
            when (val stepStr = group(1)) {
                "left"  -> Left
                "right" -> Right
                else    -> error("Next cursor step must be left or right, but was $stepStr")
            }
        }
        s = lines[lineIndex++]
        val newState = with(newStatePattern.matcher(s)) {
            require(matches()) { "Line must define next state, but was $s" }
            State(group(1))
        }
        return Transition(state, cellValue, newState, newCellValue, nextCursorStep)
    }
}

private fun readInput(inputFileName: String): TuringMachineEmulationInput =
    TuringMachineEmulationInputParser(readInputLines(2017, 25, inputFileName)).parseTuringMachineEmulationInput()

private fun printResult(inputFileName: String) {
    val turingMachineEmulationInput = readInput(inputFileName)
//    println(turingMachineEmulationInput)
    val solver = TuringMachineEmulator()

    //  Part 1
    with(turingMachineEmulationInput) {
        val res = solver.emulateAndGetChecksum(turingMachine, initState, numOfSteps)
        println("Checksum after performing $numOfSteps step(s): $res")
    }

}

fun main() {
    printResult("input.txt")
}
