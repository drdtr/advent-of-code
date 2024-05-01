package advent_of_code_2016.day12

import Util.readInputLines
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Constant
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Copy
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Dec
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Expr
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Inc
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Instruction
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Jump
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Register
import advent_of_code_2016.day12.AssembunnyEmulator.Registers.Companion.registersOf

/**
 * [Leonardo's Monorail](https://adventofcode.com/2016/day/12)
 *
 * Emulate a given program in *Assembunny* that supports following instructions:
 * - `cpy x y` - copy `x` (either an integer or the value of a register) into register `y`
 * - `inc x` - increment the value of register `x`
 * - `dec x` - decrement the value of register `x`
 * - `jnz x y` - jump to an instruction `y` away (positive means forward; negative means backward), if `x` is not `0`
 *
 * After finishing the emulation, return the resulting registers.
 */
class AssembunnyEmulator {
    @JvmInline
    value class Registers(val registers: Map<Char, Int>) {
        companion object {
            fun registersOf(vararg registerValues: Pair<Char, Int>) = Registers(mapOf(*registerValues))
        }
    }

    object Assembunny {
        sealed interface Expr
        data class Constant(val value: Int) : Expr
        data class Register(val name: Char) : Expr

        sealed interface Instruction
        data class Copy(val valueExpr: Expr, val destRegister: Register) : Instruction
        data class Inc(val register: Register) : Instruction
        data class Dec(val register: Register) : Instruction
        data class Jump(val conditionExpr: Expr, val offsetExpr: Expr) : Instruction
    }

    fun emulate(instructions: List<Instruction>, initRegisters: Registers = registersOf()): Registers =
        Emulator(instructions, initRegisters).emulate()

    private class Emulator(val instructions: List<Instruction>, initRegisters: Registers) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0

        fun emulate(): Registers {
            while (instructionPointer in instructions.indices) {
                executeCurrentInstruction()
            }
            return Registers(registers)
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Copy -> registers[i.destRegister.name] = evaluate(i.valueExpr)
                is Inc  -> registers[i.register.name] = evaluate(i.register) + 1
                is Dec  -> registers[i.register.name] = evaluate(i.register) - 1
                is Jump -> if (evaluate(i.conditionExpr) != 0) {
                    instructionPointer += -1 + evaluate(i.offsetExpr)
                }
            }
        }

        private fun evaluate(expr: Expr): Int = when (expr) {
            is Constant -> expr.value
            is Register -> registers[expr.name] ?: 0
        }
    }
}

private fun readInput(inputFileName: String): List<Instruction> {
    fun parseRegister(s: String) =
        if (s.length == 1) {
            Register(s[0])
        } else {
            error("Expected int constant or one-letter register name but was $s")
        }

    fun parseExpr(s: String) = try {
        Constant(s.toInt())
    } catch (e: NumberFormatException) {
        parseRegister(s)
    }
    return readInputLines(2016, 12, inputFileName)
            .map { it.split(" ", ",").filter { it.isNotEmpty() } }
            .map { tokens ->
                when (tokens[0]) {
                    "cpy" -> Copy(parseExpr(tokens[1]), parseRegister(tokens[2]))
                    "inc" -> Inc(parseRegister(tokens[1]))
                    "dec" -> Dec(parseRegister(tokens[1]))
                    "jnz" -> Jump(parseExpr(tokens[1]), parseExpr(tokens[2]))
                    else  -> error("Invalid instruction \"$tokens\"")
                }
            }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    val solver = AssembunnyEmulator()

    val registersPart1 = solver.emulate(instructions)
    println("Processor register values: $registersPart1")

    val registersPart2 = solver.emulate(instructions, registersOf('c' to 1))
    println("Processor register values: $registersPart2")
}

fun main() {
    printResult("input.txt")
}

