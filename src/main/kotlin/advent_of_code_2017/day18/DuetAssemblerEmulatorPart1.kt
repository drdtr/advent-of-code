package advent_of_code_2017.day18

import advent_of_code_2017.day18.DuetAssembler.Add
import advent_of_code_2017.day18.DuetAssembler.Constant
import advent_of_code_2017.day18.DuetAssembler.Expr
import advent_of_code_2017.day18.DuetAssembler.Instruction
import advent_of_code_2017.day18.DuetAssembler.Jump
import advent_of_code_2017.day18.DuetAssembler.Mod
import advent_of_code_2017.day18.DuetAssembler.Mult
import advent_of_code_2017.day18.DuetAssembler.Rcv
import advent_of_code_2017.day18.DuetAssembler.Register
import advent_of_code_2017.day18.DuetAssembler.Registers
import advent_of_code_2017.day18.DuetAssembler.Registers.Companion.registersOf
import advent_of_code_2017.day18.DuetAssembler.Set
import advent_of_code_2017.day18.DuetAssembler.Snd

/**
 * [Duet - Part 1](https://adventofcode.com/2017/day/18)
 *
 * Emulate a given program in an assembly language *Duet* that supports following instructions:
 * - `snd X` - plays a sound with frequency `X`.
 * - `set X Y` - sets register `X` to value `Y`.
 * - `add X Y` - add value `Y` to register `X`.
 * - `mul X Y` - multiply register `X` by value `Y`.
 * - `mod X Y` - set register `X` to the remainder of dividing it by value `Y`.
 * - `rcv X` - recover the frequency of the last sound played, if the value of the register `X` is not zero.
 * - `jgz X Y` - jump with offset `Y`, if the value of the register `X` is greater than zero.
 *
 * After finishing the emulation, return the first recovered frequency.
 */
class DuetAssemblerEmulatorPart1 {
    fun emulateUntilFirstRecoveredValue(instructions: List<Instruction>): Int =
        Emulator(instructions).emulateUntilFirstRecoveredValue().toInt()

    private class Emulator(
        val instructions: List<Instruction>,
        initRegisters: Registers = registersOf(),
    ) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0
        private var lastOutputValue: Long? = null
        private var lastRecoveredValue: Long? = null

        fun emulateUntilFirstRecoveredValue(): Long {
            while (instructionPointer in instructions.indices && lastRecoveredValue == null) {
                executeCurrentInstruction()
            }
            return requireNotNull(lastRecoveredValue) { "Emulation finished by no value was recovered." }
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Snd  -> lastOutputValue = evaluate(i.expr)
                is Set  -> registers[i.register.name] = evaluate(i.valueExpr)
                is Add  -> registers[i.register.name] = evaluate(i.register) + evaluate(i.valueExpr)
                is Mult -> registers[i.register.name] = evaluate(i.register) * evaluate(i.valueExpr)
                is Mod  -> registers[i.register.name] = evaluate(i.register) % evaluate(i.valueExpr)
                is Jump -> if (evaluate(i.conditionExpr) > 0) {
                    instructionPointer += (-1 + evaluate(i.offsetExpr)).toInt()
                }

                is Rcv  -> if (evaluate(i.register) != 0L) {
                    lastRecoveredValue = lastOutputValue
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
    val instructions = DuetAssembler.readInput(inputFileName)
    val solver = DuetAssemblerEmulatorPart1()

    val firstRecoveredValue = solver.emulateUntilFirstRecoveredValue(instructions)
    println("First recovered value: $firstRecoveredValue")
}

fun main() {
    printResult("input.txt")
}

