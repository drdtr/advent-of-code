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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/**
 * [Duet - Part 1](https://adventofcode.com/2017/day/18)
 *
 * Emulate the given program in the assembly language *Duet* that differs from the definition in [DuetAssemblerEmulatorPart1]
 * only in the meaning of `snd` and `rcv`, assuming that there are two programs running concurrently:
 * - `snd X` - sends the value of `X` to the other program.
 * - `rcv X` - receives the next value in the queue and stores it in the register `X`.
 *
 * Each program has its own id, `0` or `1`, resp. The register `p` contains the program id at the beginning.
 * The programs run concurrently, not necessarily at the same speed.
 * A program terminates if the instruction pointer is no longer pointing at a valid instruction,
 * or if both programs reach a deadlock.
 *
 * Once both programs have terminated, return how many times the program 1 has sent a value.
 */
class DuetAssemblerEmulatorPart2 {
    fun emulateTwoProgramsAndCountSendsOf2ndProgram(instructions: List<Instruction>): Int = runBlocking {
        val channel0to1 = Channel<Long>(capacity = UNLIMITED)
        val channel1to0 = Channel<Long>(capacity = UNLIMITED)
        val emulator0 = Emulator(
            instructions,
            registersOf('p' to 0),
            channelSend = channel0to1,
            channelReceive = channel1to0,
        )
        val emulator1 = Emulator(
            instructions,
            registersOf('p' to 1),
            channelSend = channel1to0,
            channelReceive = channel0to1,
        )
        val job0 = launch {
            emulator0.emulateAndCountSends()
        }
        val job1 = launch {
            emulator1.emulateAndCountSends()
        }
        runBlocking {
            while (!(job0.isCompleted && job1.isCompleted)) {
                val opCount0 = emulator0.opCount
                val opCount1 = emulator1.opCount
                delay(20)
                if (emulator0.opCount == opCount0 && emulator1.opCount == opCount1) {
                    // both coroutines are deadlocked
                    job0.cancel()
                    job1.cancel()
                    break
                }
            }
        }
        job0.join()
        job1.join()
        emulator1.numOfSends
    }

    private class Emulator(
        val instructions: List<Instruction>,
        initRegisters: Registers = registersOf(),
        val channelSend: Channel<Long>,
        val channelReceive: Channel<Long>,
    ) {
        private val registers = initRegisters.registers.toMutableMap()
        private var instructionPointer = 0
        var numOfSends = 0
            private set
        var opCount = 0
            private set

        suspend fun emulateAndCountSends() {
            try {
                while (instructionPointer in instructions.indices) {
                    yield() // check for cancellation
                    executeCurrentInstruction()
                    opCount++
                }
            } catch (e: CancellationException) {
                // no action needed
            }
        }

        private suspend fun executeCurrentInstruction() {
            when (val i = instructions[instructionPointer++]) {
                is Set  -> registers[i.register.name] = evaluate(i.valueExpr)
                is Add  -> registers[i.register.name] = evaluate(i.register) + evaluate(i.valueExpr)
                is Mult -> registers[i.register.name] = evaluate(i.register) * evaluate(i.valueExpr)
                is Mod  -> registers[i.register.name] = evaluate(i.register) % evaluate(i.valueExpr)
                is Jump -> if (evaluate(i.conditionExpr) > 0) {
                    instructionPointer += (-1 + evaluate(i.offsetExpr)).toInt()
                }

                is Snd  -> {
                    channelSend.send(evaluate(i.expr))
                    numOfSends++
                }

                is Rcv  -> registers[i.register.name] = channelReceive.receive()
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
    val solver = DuetAssemblerEmulatorPart2()

    val numOfSends = solver.emulateTwoProgramsAndCountSendsOf2ndProgram(instructions)
    println("Number of times the 2nd program sent a value: $numOfSends")
}

fun main() {
    printResult("input.txt")
}

