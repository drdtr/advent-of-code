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

class DuetAssemblerEmulatorPart1Test {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDuetAssemblerEmulator::class)
    fun `test emulateUntilFirstRecoveredValue`(expected: Int, instructions: List<Instruction>) =
        with(DuetAssemblerEmulatorPart1()) {
            assertEquals(expected, emulateUntilFirstRecoveredValue(instructions))
        }

    private class ArgumentsProviderDuetAssemblerEmulator : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                4, listOf(
                    set('a', 1),
                    add('a', 2),
                    mult('a', 'a'),
                    mod('a', 5),
                    snd('a'),
                    set('a', 0),
                    rcv('a'),
                    jump('a', -1),
                    set('a', 1),
                    jump('a', -2),
                )
            )
        )
    }

    companion object {
        fun snd(register: Char) = Snd(Register(register))
        fun set(register: Char, value: Int) = Set(Register(register), Constant(value))
        fun add(register: Char, value: Int) = Add(Register(register), Constant(value))
        fun mult(register: Char, value: Int) = Mult(Register(register), Constant(value))
        fun mult(register: Char, valueRegister: Char) = Mult(Register(register), Register(valueRegister))
        fun mod(register: Char, value: Int) = Mod(Register(register), Constant(value))
        fun jump(register: Char, offset: Int) = Jump(Register(register), Constant(offset))
        fun rcv(register: Char) = Rcv(Register(register))
    }
}