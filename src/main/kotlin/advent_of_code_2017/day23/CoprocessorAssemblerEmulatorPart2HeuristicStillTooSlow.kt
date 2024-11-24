package advent_of_code_2017.day18

import advent_of_code_2017.day23.CoprocessorAssembler.Constant
import advent_of_code_2017.day23.CoprocessorAssembler.Expr
import advent_of_code_2017.day23.CoprocessorAssembler.Instruction
import advent_of_code_2017.day23.CoprocessorAssembler.Jump
import advent_of_code_2017.day23.CoprocessorAssembler.Mult
import advent_of_code_2017.day23.CoprocessorAssembler.Register
import advent_of_code_2017.day23.CoprocessorAssembler.Registers
import advent_of_code_2017.day23.CoprocessorAssembler.Registers.Companion.registersOf
import advent_of_code_2017.day23.CoprocessorAssembler.Set
import advent_of_code_2017.day23.CoprocessorAssembler.Sub
import advent_of_code_2017.day23.CoprocessorAssembler.readInput
import kotlin.math.abs

/**
 * [Coprocessor Conflagration](https://adventofcode.com/2017/day/18)
 *
 * Emulate a given program in an assembly language that supports following instructions:
 * - `set X Y` - sets register `X` to value `Y`.
 * - `sub X Y` - subtracts value `Y` from register `X`.
 * - `mul X Y` - multiply register `X` by value `Y`.
 * - `jnz X Y` - jump with offset `Y`, if the value of the register `X` is not zero.
 *
 * ### Part 1
 * - All registers start at 0
 * - Count how many times the instruction `mul` is invoked.
 *
 * ### Part 2
 * - All registers start at 0, except for register `a` that starts at 1.
 * - What is the value in register `h` after the program completes?
 *
 *
 */
class CoprocessorAssemblerEmulatorPart2HeuristicNotQuite {

    fun emulateAndReturnRegisters(
        instructions: List<Instruction>,
        initRegisters: Registers = registersOf()
    ): Map<Char, Long> = Emulator(instructions, initRegisters).emulate().registers

    private data class EmulationResult(val registers: Map<Char, Long>)

    private class Emulator(
        val instructions: List<Instruction>,
        initRegisters: Registers = registersOf(),
    ) {
        private val numOfRegisters = 'h' - 'a' + 1
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0

        private val instructionPointerCounts = LongArray(instructions.size)
        private val instructionPointerAtLastCall = LongArray(instructions.size)
        private val registerValuesAtLastCall = hashMapOf<Int, LongArray>()
        private val registerValuesDiffsAtLastCall = hashMapOf<Int, LongArray>()
        private var instructionCount = 0L

        fun emulate(): EmulationResult {
            while (instructionPointer in instructions.indices) {
                instructionPointerCounts[instructionPointer]++
                printInstructionAndRegistersIfNotCalledForLongTime()
                instructionPointerAtLastCall[instructionPointer] = instructionCount
                val instruction = instructions[instructionPointer]
                if (instruction is Jump && instruction.conditionExpr is Register && instruction.conditionExpr.name == 'g'
                    && instructionPointer == 23 && heuristicsRegisterDiffsTheSameForTheLastTwoCalls()
                ) {
                    // Heuristics: if the register diff is the same as in the previous call, then we assume that we can
                    // shortcut the computation and just apply the register diff, until the value in the condition
                    // register for the Jump instruction reaches 0.
                    if (abs(evaluate(instruction.conditionExpr)) > 20L) {
                        repeat(17) { applyRegisterDiffAtLastCall() }
                    }
                }
                executeCurrentInstruction()
                if (++instructionCount % 10_000 == 0L) {
//                    println("Instruction counts after step $instructionCount: ${instructionPointerCounts.contentToString()}")
                }
            }
            10.toBigInteger().isProbablePrime(10)
            return EmulationResult(Registers(registers).registers)
        }

        private fun printInstructionAndRegistersIfNotCalledForLongTime() {
            val instruction = instructions[instructionPointer]
            val stepsSinceLastCall = instructionCount - instructionPointerAtLastCall[instructionPointer]
            if (instruction is Jump && stepsSinceLastCall >= 1_000_000) {
                val registerValuesAtPreviousCall = registerValuesAtLastCall[instructionPointer] ?: return
                val registerValueDiffs = registers.map { (r, v) -> v - (registerValuesAtPreviousCall[r - 'a']) }
                println(
                    "Step $instructionCount: " +
                            "Instruction $instructionPointer $instruction called again after $stepsSinceLastCall steps. " +
                            "\n  Registers: $registers" +
                            "\n  Register value diffs since last call: $registerValueDiffs"
                )
            }
        }

        private fun heuristicsRegisterDiffsTheSameForTheLastTwoCalls(): Boolean {
            val registerValuesAtPreviousCall = registerValuesAtLastCall[instructionPointer]
            registerValuesAtLastCall[instructionPointer] = registerMapToArray(registers)
            registerValuesAtPreviousCall ?: return false
            val registerValueDiffs =
                LongArray(numOfRegisters) { (registers['a' + it] ?: 0) - registerValuesAtPreviousCall[it] }
            return (registerValueDiffs.contentEquals(registerValuesDiffsAtLastCall[instructionPointer])).also { isSameDiff ->
                if (isSameDiff) {
//                    val instruction = instructions[instructionPointer]
//                    println(
//                        "Same register value diffs as in last call " +
//                                "for instruction $instructionPointer $instruction, " +
//                                "register value diffs: ${registerValueDiffs.contentToString()}"
//                    )
                } else {
                    registerValuesDiffsAtLastCall[instructionPointer] = registerValueDiffs
                }
                registerValuesAtLastCall[instructionPointer] = registerMapToArray(registers)
            }
        }

        private fun registerMapToArray(registerMap: Map<Char, Long>) = LongArray(numOfRegisters).apply {
            registerMap.forEach { (r, v) -> this[r - 'a'] = v }
        }

        private fun applyRegisterDiffAtLastCall() {
            val registerValueDiffs = registerValuesDiffsAtLastCall[instructionPointer] ?: return
            for ((r, v) in registers) {
                registers[r] = (registers[r] ?: 0) + registerValueDiffs[r - 'a']
            }
            val instruction = instructions[instructionPointer]
            if (instruction is Jump && evaluate(instruction.conditionExpr) % 1000 == 0L) {
                println(
                    "Step $instructionCount: Instruction $instructionPointer $instruction. " +
                            "\n  Register diff heuristically applied: ${registerValueDiffs.contentToString()}" +
                            "\n  Registers: $registers"
                )
            }
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Set -> registers[i.register.name] = evaluate(i.valueExpr)
                is Sub -> registers[i.register.name] = evaluate(i.register) - evaluate(i.valueExpr)
                is Mult -> registers[i.register.name] = evaluate(i.register) * evaluate(i.valueExpr)
                is Jump -> if (evaluate(i.conditionExpr) != 0L) {
                    instructionPointer += (-1 + evaluate(i.offsetExpr)).toInt()
                }
            }
        }

        private fun evaluate(expr: Expr): Long = when (expr) {
            is Constant -> expr.value.toLong()
            is Register -> registers[expr.name] ?: 0
        }
    }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    val regH = CoprocessorAssemblerEmulatorPart2HeuristicNotQuite().emulateAndReturnRegisters(
        instructions,
        registersOf('a' to 1)
    )
    println("Value in register 'h': $regH")
}

fun main() {
    printResult("input.txt")
}

