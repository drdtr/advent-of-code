package advent_of_code_2017.day18

import Util.readInputLines
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Add
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Constant
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Expr
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Instruction
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Jump
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Mod
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Mult
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Rcv
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Register
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Set
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Snd
import advent_of_code_2017.day18.DuetAssemblerEmulator.Registers.Companion.registersOf

/**
 * [Duet - Part 1](https://adventofcode.com/2017/day/18)
 *
 * Emulate a given program in an assembly language *Duet that supports following instructions:
 * - `snd X`- plays a sound with frequency `X`.
 * - `set X Y` - sets register `X` to value `Y`.
 * - `add X Y` - add value `Y` to register `X`.
 * - `mul X Y` - multiply register `X` by value `Y`.
 * - `mod X Y` - set register `X` to the remainder of dividing it by value `Y`.
 * - `rcv X` - recover the frequency of the last sound played, if the value of the register `X` is not zero.
 * - `jgz X Y` - jump with offset `Y`, if the value of the register `X` is greater than zero.
 *
 * After finishing the emulation, return the first recovered frequency.
 */
class DuetAssemblerEmulator {
    @JvmInline
    value class Registers(val registers: Map<Char, Long>) {
        companion object {
            fun registersOf(vararg registerValues: Pair<Char, Long>) = Registers(mapOf(*registerValues))
        }
    }

    object DuetAssembler {
        sealed interface Expr
        data class Constant(val value: Int) : Expr
        data class Register(val name: Char) : Expr

        sealed interface Instruction
        data class Snd(val register: Register) : Instruction
        data class Set(val register: Register, val valueExpr: Expr) : Instruction
        data class Add(val register: Register, val valueExpr: Expr) : Instruction
        data class Mult(val register: Register, val valueExpr: Expr) : Instruction
        data class Mod(val register: Register, val valueExpr: Expr) : Instruction
        data class Rcv(val register: Register) : Instruction
        data class Jump(val conditionExpr: Expr, val offsetExpr: Expr) : Instruction
    }

    fun emulateUntilFirstRecoveredValue(
        instructions: List<Instruction>,
        initRegisters: Registers = registersOf(),
    ): Int = Emulator(instructions, initRegisters).emulateUntilFirstRecoveredValue().toInt()

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
                is Snd  -> lastOutputValue = evaluate(i.register)
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

private fun readInput(inputFileName: String): List<Instruction> {
    fun parseRegister(s: String) =
        if (s.length == 1) {
            Register(s[0])
        } else {
            error("Expected integer constant or one-letter register name but was $s")
        }

    fun parseExpr(s: String) = try {
        Constant(s.toInt())
    } catch (e: NumberFormatException) {
        parseRegister(s)
    }
    return readInputLines(2017, 18, inputFileName)
            .map { line -> line.split(" ", ",").filter { it.isNotEmpty() } }
            .map { tokens ->
                when (tokens[0]) {
                    "snd" -> Snd(parseRegister(tokens[1]))
                    "set" -> Set(parseRegister(tokens[1]), parseExpr(tokens[2]))
                    "add" -> Add(parseRegister(tokens[1]), parseExpr(tokens[2]))
                    "mul" -> Mult(parseRegister(tokens[1]), parseExpr(tokens[2]))
                    "mod" -> Mod(parseRegister(tokens[1]), parseExpr(tokens[2]))
                    "rcv" -> Rcv(parseRegister(tokens[1]))
                    "jgz" -> Jump(parseExpr(tokens[1]), parseExpr(tokens[2]))
                    else  -> error("Invalid instruction \"$tokens\"")
                }
            }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    println(instructions.joinToString(separator = "\n"))
    val solver = DuetAssemblerEmulator()

    // part 1
    val firstRecoveredValue = solver.emulateUntilFirstRecoveredValue(instructions)
    println("First recovered value: $firstRecoveredValue")
}

fun main() {
    printResult("input.txt")
}

