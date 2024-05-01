package advent_of_code_2017.day18

import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Add
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Constant
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Instruction
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Jump
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Mod
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Mult
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Rcv
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Register
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Set
import advent_of_code_2017.day18.DuetAssemblerEmulator.DuetAssembler.Snd
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class DuetAssemblerEmulatorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDuetAssemblerEmulator::class)
    fun `test DuetAssemblerEmulator`(expected: Int, instructions: List<Instruction>) = with(DuetAssemblerEmulator()) {
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
                    sound('a'),
                    set('a', 0),
                    recover('a'),
                    jump('a', -1),
                    set('a', 1),
                    jump('a', -2),
                )
            )
        )
    }

    companion object {
        fun sound(register: Char) = Snd(Register(register))
        fun set(register: Char, value: Int) = Set(Register(register), Constant(value))
        fun add(register: Char, value: Int) = Add(Register(register), Constant(value))
        fun mult(register: Char, value: Int) = Mult(Register(register), Constant(value))
        fun mult(register: Char, valueRegister: Char) = Mult(Register(register), Register(valueRegister))
        fun mod(register: Char, value: Int) = Mod(Register(register), Constant(value))
        fun jump(register: Char, offset: Int) = Jump(Register(register), Constant(offset))
        fun recover(register: Char) = Rcv(Register(register))
    }
}