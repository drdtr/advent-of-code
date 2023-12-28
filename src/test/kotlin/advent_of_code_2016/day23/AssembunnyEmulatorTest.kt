package advent_of_code_2016.day23

import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.*
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Constant
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Copy
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Dec
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Inc
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Jump
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Register
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Assembunny.Toggle
import advent_of_code_2016.day23.AssembunnyEmulatorWithToggle.Registers.Companion.registersOf
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


internal class AssembunnyEmulatorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderAssembunnyEmulator::class)
    fun `test AssembunnyEmulator`(
        expectedValuesForSelectedRegisters: Map<Char, Int>,
        instructions: List<Assembunny.Instruction>
    ) =
        with(AssembunnyEmulatorWithToggle()) {
            val registers = emulate(instructions)

            for ((registerName, expectedValue) in expectedValuesForSelectedRegisters) {
                assertEquals(expectedValue, assertNotNull(registers.registers[registerName]))
            }
        }

    private class ArgumentsProviderAssembunnyEmulator : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                registersOf('a' to 3),
                listOf(
                    copy(2, 'a'),
                    toggle('a'),
                    toggle('a'),
                    toggle('a'),
                    copy(1, 'a'),
                    dec('a'),
                    dec('a'),
                )
            ),
        ).map {
            // workaround for `value class` not being handled correctly in JUnit
            Arguments.of(
                (it.get()[0] as Registers).registers,
                it.get()[1]
            )
        }
    }

    companion object {
        fun copy(value: Int, destRegister: Char) = Copy(Constant(value), Register(destRegister))
        fun copy(srcRegister: Char, destRegister: Char) = Copy(Register(srcRegister), Register(destRegister))
        fun inc(register: Char) = Inc(Register(register))
        fun dec(register: Char) = Dec(Register(register))
        fun jump(condition: Int, offset: Int) = Jump(Constant(condition), Constant(offset))
        fun jump(conditionRegister: Char, offset: Int) = Jump(Register(conditionRegister), Constant(offset))
        fun jump(condition: Int, offsetRegister: Char) = Jump(Constant(condition), Register(offsetRegister))
        fun jump(conditionRegister: Char, offsetRegister: Char) =
            Jump(Register(conditionRegister), Register(offsetRegister))

        fun toggle(register: Char) = Toggle(Register(register))
        fun toggle(value: Int) = Toggle(Constant(value))
    }
}