package advent_of_code_2016.day25

import Util.readInputLines
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
import java.util.concurrent.atomic.AtomicBoolean

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

    fun createEmulatorWithCancelSwitch(
        instructions: List<Instruction>,
        initRegisters: Registers,
    ): Pair<Emulator, AtomicBoolean> {
        val cancel = AtomicBoolean(false)
        return Emulator(instructions, initRegisters, cancel) to cancel
    }

    class Emulator(
        val instructions: List<Instruction>,
        initRegisters: Registers,
        val cancel: AtomicBoolean = AtomicBoolean(false),
    ) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0

        fun emulateOutput() = sequence {
            while (!cancel.get() && instructionPointer in instructions.indices) {
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

    // just try the first 1000 positive integers
    for (a in 1..1000) {
        var foundInitialValueForCorrectSequence = false
        val (emulator, cancel) = solver.createEmulatorWithCancelSwitch(instructions, registersOf('a' to a))
        // after these many element of the output sequence, we'd be sufficiently confident that it works
        val maxCount = 10_000
        var sequenceIndex = 0
        for (element in emulator.emulateOutput()) {
            if (cancel.get()) {
                // We've cancelled the sequence in the previous iteration, and needed just one more iteration
                // to let the sequence know. Now we can quit the loop.
                break
            }
            val expected = sequenceIndex % 2 // sequence 0, 1, 0, 1,...
            if (element != expected) {
                println("Output sequence element at $sequenceIndex for a=$a: expected $expected but was $element")
                cancel.set(true)
            }
            if (++sequenceIndex == maxCount) {
                println("Output sequence for a=$a was as expected for the first $maxCount elements")
                cancel.set(true)
                foundInitialValueForCorrectSequence = true
            }
        }
        if (foundInitialValueForCorrectSequence) {
            println("Correct output sequence found for first $maxCount elements for initial value a=$a")
            break
        }
    }
}

fun main() {
    printResult("input.txt")
}

