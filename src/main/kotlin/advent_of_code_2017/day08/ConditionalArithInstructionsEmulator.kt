package advent_of_code_2017.day08

import Util.readInputLines
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.*
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.ArithInstruction
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.ComparisonCondition
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.ConditionalArithInstruction
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Dec
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Eq
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Ge
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Gt
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Inc
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Le
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Lt
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Neq
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Registers.Companion.registersOf
import java.util.regex.Pattern
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Instruction

/**
 * [I Heard You Like Registers](https://adventofcode.com/2017/day/8)
 *
 * Emulate a list of conditional arithmetic instructions. An instruction has the form `r1 op n if r2 cmp c`:
 * - `r1` and `r2` are registers, which can be same or different
 * - `op` can be `inc` or `dec`
 * - `n` is an integer constant to be added or subtracted from `r1`
 * - `cmp` can be `<`, `<=`, `>`, `>=`, or `==`
 * - `c` is an integer constant to which `r2` is compared
 *
 * ### Part 1
 *
 * Return the largest register value after finishing all instructions.
 *
 */
class ConditionalArithInstructionsEmulator {
    @JvmInline
    value class Registers(val registers: Map<String, Int>) {
        companion object {
            fun registersOf(vararg registerValues: Pair<String, Int>) = Registers(mapOf(*registerValues))
        }
    }

    object Instructions {
        sealed interface ComparisonOp
        data object Gt : ComparisonOp
        data object Ge : ComparisonOp
        data object Lt : ComparisonOp
        data object Le : ComparisonOp
        data object Eq : ComparisonOp
        data object Neq : ComparisonOp

        sealed interface ArithOp
        data object Inc : ArithOp
        data object Dec : ArithOp

        data class ComparisonCondition(val register: String, val cmpOp: ComparisonOp, val value: Int) {
            override fun toString(): String {
                val cmpOpStr = when (cmpOp) {
                    Gt  -> ">"
                    Ge  -> ">="
                    Lt  -> "<"
                    Le  -> "<="
                    Eq  -> "=="
                    Neq -> "!="
                }
                return "$register $cmpOpStr $value"
            }
        }

        sealed interface Instruction
        data class ArithInstruction(val register: String, val arithOp: ArithOp, val value: Int) : Instruction {
            override fun toString(): String {
                val arithOpStr = when (arithOp) {
                    Inc -> "inc"
                    Dec -> "dec"
                }
                return "$register $arithOpStr $value"
            }
        }

        data class ConditionalArithInstruction(
            val arithInstr: ArithInstruction,
            val cmpCondition: ComparisonCondition,
        ) : Instruction {
            override fun toString(): String = "$arithInstr if $cmpCondition"
        }

        infix fun String.gt(value: Int) = ComparisonCondition(this, Gt, value)
        infix fun String.ge(value: Int) = ComparisonCondition(this, Ge, value)
        infix fun String.lt(value: Int) = ComparisonCondition(this, Lt, value)
        infix fun String.le(value: Int) = ComparisonCondition(this, Le, value)
        infix fun String.eq(value: Int) = ComparisonCondition(this, Eq, value)
        infix fun String.neq(value: Int) = ComparisonCondition(this, Neq, value)

        infix fun String.inc(value: Int) = ArithInstruction(this, Inc, value)
        infix fun String.dec(value: Int) = ArithInstruction(this, Dec, value)

        infix fun ArithInstruction.If(condition: ComparisonCondition) = ConditionalArithInstruction(this, condition)
    }


    fun findLargestRegisterValueAfterEmulation(instructions: List<Instruction>): Int =
        emulate(instructions).registers.registers.values.maxOrNull() ?: 0

    fun findLargestRegisterValueDuringEmulation(instructions: List<Instruction>): Int =
        emulate(instructions).maxRegisters.registers.values.maxOrNull() ?: 0

    data class ResultRegisters(val registers: Registers, val maxRegisters: Registers)

    fun emulate(instructions: List<Instruction>, initRegisters: Registers = registersOf()): ResultRegisters =
        Emulator(instructions, initRegisters).emulate()

    private class Emulator(val instructions: List<Instruction>, initRegisters: Registers) {
        private val registers = initRegisters.registers.toMutableMap()
        private val maxRegisters = registers.toMutableMap()
        private var instructionPointer = 0

        fun emulate(): ResultRegisters {
            while (instructionPointer in instructions.indices) {
                executeCurrentInstruction()
            }
            return ResultRegisters(Registers(registers), Registers(maxRegisters))
        }

        private fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is ArithInstruction            -> i.execute()
                is ConditionalArithInstruction -> if (i.cmpCondition.isTrue()) i.arithInstr.execute()
            }
        }

        private fun ComparisonCondition.isTrue(): Boolean {
            val r = registers[register] ?: 0
            return when (cmpOp) {
                is Gt  -> r > value
                is Ge  -> r >= value
                is Lt  -> r < value
                is Le  -> r <= value
                is Eq  -> r == value
                is Neq -> r != value
            }
        }

        private fun ArithInstruction.execute() {
            val r = registers[register] ?: 0
            val rNew = when (arithOp) {
                is Inc -> r + value
                is Dec -> r - value
            }
            registers[register] = rNew
            if ((maxRegisters[register] ?: 0) < rNew) maxRegisters[register] = rNew
        }
    }
}

private val conditionalArithInstructionPattern =
    Pattern.compile("(\\w+) (inc|dec) (-?\\d+) if (\\w+) (.{1,2}) (-?\\d+)")

private fun parseInstruction(s: String): Instruction {
    with(conditionalArithInstructionPattern.matcher(s)) {
        if (matches()) {
            return ConditionalArithInstruction(
                arithInstr = parseArithInstruction(group(1), group(2), group(3)),
                cmpCondition = parseComparisonCondition(group(4), group(5), group(6))
            )
        }
    }
    error("Invalid instruction $s")
}

private fun parseArithInstruction(register: String, op: String, valueStr: String) =
    ArithInstruction(
        register = register,
        arithOp = when (op) {
            "inc" -> Inc
            "dec" -> Dec
            else  -> error("Unexpected arithmetic operator $op")
        },
        value = valueStr.toInt(),
    )


private fun parseComparisonCondition(register: String, cmpOp: String, valueStr: String) = ComparisonCondition(
    register = register,
    cmpOp = when (cmpOp) {
        ">"  -> Gt
        ">=" -> Ge
        "<"  -> Lt
        "<=" -> Le
        "==" -> Eq
        "!=" -> Neq
        else -> error("Unexpected comparison operator $cmpOp")
    },
    value = valueStr.toInt(),
)

private fun readInput(inputFileName: String): List<Instruction> =
    readInputLines(2017, 8, inputFileName).map(::parseInstruction)

private fun printResult(inputFileName: String) {
    val solver = ConditionalArithInstructionsEmulator()
    val instructions = readInput(inputFileName)
    println(instructions.joinToString(separator = "\n"))
    // part 1
    val res1 = solver.findLargestRegisterValueAfterEmulation(instructions)
    println("Largest register value after all instructions: $res1")
    // part 2
    val res2 = solver.findLargestRegisterValueDuringEmulation(instructions)
    println("Largest register value during all instructions: $res2")
}

fun main() {
    printResult("input.txt")
}