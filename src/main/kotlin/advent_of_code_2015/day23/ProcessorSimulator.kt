package advent_of_code_2015.day23

import Util.readInputLines
import advent_of_code_2015.day23.ProcessorSimulator.*
import advent_of_code_2015.day23.ProcessorSimulator.Registers.Companion.registersOf

/**
 * [Opening the Turing Lock](https://adventofcode.com/2015/day/23)
 *
 * Simulate a simple processor with following set of instructions
 * and registers `a` and `b` that can hold non-negative integers:
 * - `hlf r` -> half the value in register `r`
 * - `tpl r` -> tripple the value in register `r`
 * - `inc r` -> increment the value in register `r` by 1
 * - `jmp offset` -> jump to the instruction at `offset` relative to the current instruction
 * - `jie r, offset` -> jump to the instruction at `offset` relative to the current instruction, if `r` is even
 * - `jio r, offset` -> jump to the instruction at `offset` relative to the current instruction, if `r` is equal to 1
 *
 * Given a list of instructions return the value in register `b` after the simulation terminates.
 */
class ProcessorSimulator {
    sealed interface Instruction
    data class Half(val register: String) : Instruction
    data class Triple(val register: String) : Instruction
    data class Increment(val register: String) : Instruction
    data class Jump(val offset: Int) : Instruction
    data class JumpIfEven(val register: String, val offset: Int) : Instruction
    data class JumpIfOne(val register: String, val offset: Int) : Instruction

    @JvmInline
    value class Registers(val registers: Map<String, Long>) {
        companion object {
            fun registersOf(vararg registerValues: Pair<String, Long>) = Registers(mapOf(*registerValues))
        }
    }

    fun simulate(instructions: List<Instruction>, initRegisters: Registers = Registers(emptyMap())): Registers =
        Simulator(instructions, initRegisters).simulate()

    private class Simulator(val instructions: List<Instruction>, initRegisters: Registers) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0

        fun simulate(): Registers {
            while (instructionPointer in instructions.indices) {
                executeCurrentInstruction()
            }
            return Registers(registers)
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Half       -> registers[i.register] = (registers[i.register] ?: 0) / 2
                is Triple     -> registers[i.register] = (registers[i.register] ?: 0) * 3
                is Increment  -> registers[i.register] = (registers[i.register] ?: 0) + 1
                is Jump       -> instructionPointer += -1 + i.offset
                is JumpIfEven -> if ((registers[i.register] ?: 0) % 2 == 0L) instructionPointer += -1 + i.offset
                is JumpIfOne  -> if ((registers[i.register] ?: 0) == 1L) instructionPointer += -1 + i.offset
            }
        }
    }
}


private fun readInput(inputFileName: String): List<Instruction> {
    return readInputLines(2015, 23, inputFileName)
            .map { it.split(" ", ",").filter { it.isNotEmpty() } }
            .map { tokens ->
                when (tokens[0]) {
                    "hlf" -> Half(tokens[1])
                    "tpl" -> Triple(tokens[1])
                    "inc" -> Increment(tokens[1])
                    "jmp" -> Jump(tokens[1].toInt())
                    "jie" -> JumpIfEven(tokens[1], tokens[2].toInt())
                    "jio" -> JumpIfOne(tokens[1], tokens[2].toInt())
                    else  -> error("Invalid instruction \"$tokens\"")
                }
            }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    val solver = ProcessorSimulator()

    val registersPart1 = solver.simulate(instructions)
    println("Processor register values: $registersPart1")

    val registersPart2 = solver.simulate(instructions, registersOf("a" to 1))
    println("Processor register values: $registersPart2")
}

fun main() {
    printResult("input.txt")
}
