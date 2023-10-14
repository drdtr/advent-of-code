package advent_of_code_2016.day12

import advent_of_code_2016.day12.AssembunnyEmulator.*
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Constant
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Copy
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Dec
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Inc
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Jump
import advent_of_code_2016.day12.AssembunnyEmulator.Assembunny.Register
import advent_of_code_2016.day12.AssembunnyEmulator.Registers.Companion.registersOf
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
        with(AssembunnyEmulator()) {
            val registers = emulate(instructions)

            for ((registerName, expectedValue) in expectedValuesForSelectedRegisters) {
                assertEquals(expectedValue, assertNotNull(registers.registers[registerName]))
            }
        }

    private class ArgumentsProviderAssembunnyEmulator : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                registersOf('a' to 41),
                listOf(
                    copy(41, 'a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 42),
                listOf(
                    copy(41, 'a'),
                    inc('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 43),
                listOf(
                    copy(41, 'a'),
                    inc('a'),
                    inc('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 42),
                listOf(
                    copy(41, 'a'),
                    inc('a'),
                    inc('a'),
                    dec('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 42),
                listOf(
                    copy(41, 'a'),
                    inc('a'),
                    inc('a'),
                    dec('a'),
                    jump('a', 2),
                )
            ),
            Arguments.of(
                registersOf('a' to 42),
                listOf(
                    copy(41, 'a'),
                    inc('a'),
                    inc('a'),
                    dec('a'),
                    jump('a', 2),
                    dec('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 3),
                listOf(
                    copy(1, 'a'),
                    inc('a'),
                    jump(0, 123),
                    inc('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 4),
                listOf(
                    copy(1, 'a'),
                    jump('a', 2),
                    inc('a'),
                    inc('a'),
                    inc('a'),
                    inc('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 2, 'b' to 1),
                listOf(
                    copy(1, 'a'),
                    copy('a', 'b'),
                    inc('a'),
                )
            ),
            Arguments.of(
                registersOf('a' to 4),
                listOf(
                    copy(0, 'a'),
                    copy(100, 'b'),
                    jump('a', 'b'),
                    copy(2, 'b'),
                    jump(-1, 'b'),
                    inc('a'),
                    inc('a'),
                    inc('a'),
                    inc('a'),
                    inc('a'),
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
    }
}