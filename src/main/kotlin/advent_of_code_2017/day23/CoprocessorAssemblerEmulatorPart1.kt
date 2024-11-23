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

/**
 * [Coprocessor Conflagration - Part 1](https://adventofcode.com/2017/day/18)
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
 */
class CoprocessorAssemblerEmulatorPart1 {
    fun emulateUntilFirstRecoveredValue(instructions: List<Instruction>): Int =
        Emulator(instructions).emulateAndReturnMultCount()

    private class Emulator(
        val instructions: List<Instruction>,
        initRegisters: Registers = registersOf(),
    ) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0
        private var multCount = 0

        fun emulateAndReturnMultCount(): Int {
            while (instructionPointer in instructions.indices) {
                executeCurrentInstruction()
            }
            return multCount
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Set  -> registers[i.register.name] = evaluate(i.valueExpr)
                is Sub  -> registers[i.register.name] = evaluate(i.register) - evaluate(i.valueExpr)
                is Mult -> {
                    registers[i.register.name] = evaluate(i.register) * evaluate(i.valueExpr)
                    multCount++
                }

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

    val firstRecoveredValue = CoprocessorAssemblerEmulatorPart1().emulateUntilFirstRecoveredValue(instructions)
    println("Number of mult invocations: $firstRecoveredValue")
}

fun main() {
    printResult("input.txt")
}