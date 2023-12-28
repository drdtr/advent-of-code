package advent_of_code_2016.day23

import Util.readInputLines
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Constant
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Copy
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.CopyInvalid
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Dec
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.DecInvalid
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Expr
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Inc
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.IncInvalid
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Instruction
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Jump
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Register
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Toggle
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Registers.Companion.registersOf

/**
 * [Safe Cracking](https://adventofcode.com/2016/day/12)
 *
 * Emulate a given program in *Assembunny*, like in [advent_of_code_2016.day12.AssembunnyEmulator],
 * which also has with a `toggle` instruction: `tgl x` toggles the instruction `x` away, similar to `jnz`:
 * - for a `1-argument` instruction
 *    - `inc` becomes `dec`
 *    - any other instruction becomes `inc`
 * - for a `2-argument` instruction
 *    - `jnz` becomes `cpy`
 *    - any other instruction becomes `jnz`
 * - attempting to toggle in instruction outside the program is ignored
 * - if toggling produces an invalid instruction, e.g. `cpy 1 2`,
 * then it would be ignored and skipped if an attempt is made later to execute it
 *
 * After finishing the emulation, return the resulting registers.
 */
class AssembunnyEmulatorWithToggle {
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
        data class Toggle(val offsetExpr: Expr) : Instruction
        data class IncInvalid(val constant: Constant) : Instruction
        data class DecInvalid(val constant: Constant) : Instruction
        data class CopyInvalid(val valueExpr: Expr, val destConstant: Constant) : Instruction
    }

    fun emulate(instructions: List<Instruction>, initRegisters: Registers = registersOf()): Registers =
        Emulator(instructions, initRegisters).emulate()

    private class Emulator(initInstructions: List<Instruction>, initRegisters: Registers) {
        private val registers = initRegisters.registers.toMutableMap()
        val instructions = initInstructions.toMutableList()
        private var instructionPointer = 0

        fun emulate(): Registers {
            while (instructionPointer in instructions.indices) {
                executeCurrentInstruction()
            }
            return Registers(registers)
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Toggle -> toggle(evaluate(i.offsetExpr))
                is Copy   -> registers[i.destRegister.name] = evaluate(i.valueExpr)
                is Inc    -> registers[i.register.name] = (registers[i.register.name] ?: 0) + 1
                is Dec    -> registers[i.register.name] = (registers[i.register.name] ?: 0) - 1
                is Jump   -> if (evaluate(i.conditionExpr) != 0) instructionPointer += -1 + evaluate(i.offsetExpr)
                else      -> {}
            }
        }

        private fun evaluate(expr: Expr): Int = when (expr) {
            is Constant -> expr.value
            is Register -> registers[expr.name] ?: 0
        }

        private fun toggle(offset: Int) {
            val pos = instructionPointer - 1 + offset
            if (pos !in instructions.indices) return
            instructions[pos] = when (val i = instructions[pos]) {
                is Inc         -> Dec(i.register)
                is IncInvalid  -> DecInvalid(i.constant)
                is Dec         -> Inc(i.register)
                is DecInvalid  -> IncInvalid(i.constant)
                is Copy        -> Jump(i.valueExpr, i.destRegister)
                is CopyInvalid -> Jump(i.valueExpr, i.destConstant)
                is Toggle      -> when (i.offsetExpr) {
                    is Register -> Inc(i.offsetExpr)
                    is Constant -> IncInvalid(i.offsetExpr)
                }

                is Jump        -> when (i.offsetExpr) {
                    is Register -> Copy(i.conditionExpr, i.offsetExpr)
                    is Constant -> CopyInvalid(i.conditionExpr, i.offsetExpr)
                }
            }
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
    return readInputLines(2016, 23, inputFileName)
            .map { it.split(" ", ",").filter { it.isNotEmpty() } }
            .map { tokens ->
                when (tokens[0]) {
                    "cpy" -> Copy(parseExpr(tokens[1]), parseRegister(tokens[2]))
                    "inc" -> Inc(parseRegister(tokens[1]))
                    "dec" -> Dec(parseRegister(tokens[1]))
                    "jnz" -> Jump(parseExpr(tokens[1]), parseExpr(tokens[2]))
                    "tgl" -> Toggle(parseExpr(tokens[1]))
                    else  -> error("Invalid instruction \"$tokens\"")
                }
            }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    val solver = AssembunnyEmulatorWithToggle()

    val registersPart1 = solver.emulate(instructions, registersOf('a' to 7))
    println("Processor register values: $registersPart1")

    val registersPart2 = solver.emulate(instructions, registersOf('a' to 12))
    println("Processor register values: $registersPart2")
}

fun main() {
    printResult("input.txt")
}

