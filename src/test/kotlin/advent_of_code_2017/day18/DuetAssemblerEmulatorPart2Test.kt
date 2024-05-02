package advent_of_code_2017.day18

import advent_of_code_2017.day18.DuetAssembler.Add
import advent_of_code_2017.day18.DuetAssembler.Constant
import advent_of_code_2017.day18.DuetAssembler.Instruction
import advent_of_code_2017.day18.DuetAssembler.Jump
import advent_of_code_2017.day18.DuetAssembler.Mod
import advent_of_code_2017.day18.DuetAssembler.Mult
import advent_of_code_2017.day18.DuetAssembler.Rcv
import advent_of_code_2017.day18.DuetAssembler.Register
import advent_of_code_2017.day18.DuetAssembler.Set
import advent_of_code_2017.day18.DuetAssembler.Snd
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class DuetAssemblerEmulatorPart2Test {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDuetAssemblerEmulator::class)
    fun `test DuetAssemblerEmulator`(expected: Int, instructions: List<Instruction>) =
        with(DuetAssemblerEmulatorPart2()) {
            assertEquals(expected, emulateTwoProgramsAndCountSendsOf2ndProgram(instructions))
        }

    private class ArgumentsProviderDuetAssemblerEmulator : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                2, listOf(
                    snd(1),
                    snd('p'),
                )
            ),
            arguments(
                2, listOf(
                    snd(1),
                    snd('p'),
                    rcv('a'),
                )
            ),
            arguments(
                2, listOf(
                    snd(1),
                    snd('p'),
                    rcv('a'),
                    rcv('b'),
                )
            ),
            arguments(
                2, listOf(
                    snd(1),
                    snd('p'),
                    rcv('a'),
                    rcv('b'),
                    rcv('c'),
                )
            ),
            arguments(
                3, listOf(
                    snd(1),
                    snd(2),
                    snd('p'),
                    rcv('a'),
                    rcv('b'),
                    rcv('c'),
                    rcv('d'),
                )
            ),
        )
    }

    companion object {
        fun snd(register: Char) = Snd(Register(register))
        fun snd(value: Int) = Snd(Constant(value))
        fun set(register: Char, value: Int) = Set(Register(register), Constant(value))
        fun add(register: Char, value: Int) = Add(Register(register), Constant(value))
        fun mult(register: Char, value: Int) = Mult(Register(register), Constant(value))
        fun mult(register: Char, valueRegister: Char) = Mult(Register(register), Register(valueRegister))
        fun mod(register: Char, value: Int) = Mod(Register(register), Constant(value))
        fun jump(register: Char, offset: Int) = Jump(Register(register), Constant(offset))
        fun rcv(register: Char) = Rcv(Register(register))
    }
}