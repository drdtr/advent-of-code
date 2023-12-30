package advent_of_code_2016.day25

import Util.readInputLines
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.*
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Constant
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Copy
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Dec
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Expr
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Inc
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Instruction
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Jump
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Out
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Assembunny.Register
import advent_of_code_2016.day25.AssembunnyEmulatorWithOutput.Registers.Companion.registersOf

/**
 * [Clock Signal](https://adventofcode.com/2016/day/25)
 *
 * Emulate a given program in *Assembunny*, like in [advent_of_code_2016.day12.AssembunnyEmulator],
 * which also has a `out` instruction: `out x` transmits `x`, which can be an integer or a register.
 *
 * For a given program, what is the lowest positive initial value for the register `a`
 * so that the emulation transmits a sequence `0, 1, 0, 1,...` forever.
 */
class AssembunnyEmulatorWithOutput {
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
        data class Out(val valueExpr: Expr) : Instruction
    }

    sealed interface OutputCheckingResult
    object ExpectedOutput : OutputCheckingResult
    data class UnexpectedOutput(val outputPos: Int, val expected: Int, val actual: Int) : OutputCheckingResult

    fun emulateAndCheckOutput(
        instructions: List<Instruction>,
        initRegisters: Registers,
        expectedOutput: List<Int>,
    ): OutputCheckingResult {
        val emulator = Emulator(instructions, initRegisters)
        var pos = -1
        for (element in emulator.emulateOutput()) {
            if (++pos == expectedOutput.size) break
            val expected = expectedOutput[pos]
            if (element != expected) return UnexpectedOutput(outputPos = pos, expected = expected, actual = element)
        }
        return ExpectedOutput
    }

    private class Emulator(val instructions: List<Instruction>, initRegisters: Registers) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0

        fun emulateOutput() = sequence {
            while (instructionPointer in instructions.indices) {
                when (val i = instructions[instructionPointer++]) {
                    is Copy -> registers[i.destRegister.name] = evaluate(i.valueExpr)
                    is Inc  -> registers[i.register.name] = (registers[i.register.name] ?: 0) + 1
                    is Dec  -> registers[i.register.name] = (registers[i.register.name] ?: 0) - 1
                    is Jump -> if (evaluate(i.conditionExpr) != 0) instructionPointer += -1 + evaluate(i.offsetExpr)
                    is Out  -> yield(evaluate(i.valueExpr))
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
    return readInputLines(2016, 25, inputFileName)
            .map { it.split(" ", ",").filter { it.isNotEmpty() } }
            .map { tokens ->
                when (tokens[0]) {
                    "cpy" -> Copy(parseExpr(tokens[1]), parseRegister(tokens[2]))
                    "inc" -> Inc(parseRegister(tokens[1]))
                    "dec" -> Dec(parseRegister(tokens[1]))
                    "jnz" -> Jump(parseExpr(tokens[1]), parseExpr(tokens[2]))
                    "out" -> Out(parseExpr(tokens[1]))
                    else  -> error("Invalid instruction \"$tokens\"")
                }
            }
}

private fun printResult(inputFileName: String) {
    val instructions = readInput(inputFileName)
    val solver = AssembunnyEmulatorWithOutput()

    // After 10_000 elements of the output sequence, we'd be sufficiently confident to be getting the wanted output.
    // This won't increase the runtime for emulations where the actual output sequence is not as expected,
    // because the output will typically diverge already after a few elements,
    // so we won't be checking 10_000 elements for these cases.
    val expectedOutput = List(10_000) { it % 2 } // sequence 0, 1, 0, 1,...
    // try the first 1000 positive integers as initial value for the register `a`
    for (a in 1..1000) {
        var foundInitialValueForCorrectSequence = false
        when (val outputCheckRes = solver.emulateAndCheckOutput(instructions, registersOf('a' to a), expectedOutput)) {
            is UnexpectedOutput -> with(outputCheckRes) {
                println("Output sequence element at $outputPos for a=$a: expected $expected but was $actual")
            }

            is ExpectedOutput   -> {
                println("For the initial register value a=$a, the output sequence was as expected for the first ${expectedOutput.size} elements")
                break
            }
        }
    }
}

fun main() {
    printResult("input.txt")
}

