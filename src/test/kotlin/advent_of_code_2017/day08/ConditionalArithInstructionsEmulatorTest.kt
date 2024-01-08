package advent_of_code_2017.day08

import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.*
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.If
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.Instruction
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.dec
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.eq
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.ge
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.gt
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.inc
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.le
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.lt
import advent_of_code_2017.day08.ConditionalArithInstructionsEmulator.Instructions.neq
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConditionalArithInstructionsEmulatorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderEmulator::class)
    fun `test emulate`(
        expectedValuesForSelectedRegisters: Map<String, Int>,
        instructions: List<Instruction>
    ) =
        with(ConditionalArithInstructionsEmulator()) {
            val registers = emulate(instructions).registers

            for ((registerName, expectedValue) in expectedValuesForSelectedRegisters) {
                assertEquals(expectedValue, assertNotNull(registers.registers[registerName]))
            }
        }

    private class ArgumentsProviderEmulator : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                Registers.registersOf("a" to 1, "c" to -10),
                listOf(
                    "b" inc 5 If ("a" gt 1),
                    "a" inc 1 If ("b" lt 4),
                    "c" dec -10 If ("a" ge 1),
                    "c" inc -20 If ("c" eq 10),
                )
            ),
            Arguments.of(
                Registers.registersOf("a" to 1),
                listOf(
                    "b" inc 5 If ("a" gt 1),
                    "a" inc 1 If ("b" lt 4),
                )
            ),
            Arguments.of(
                Registers.registersOf("a" to 1, "b" to 5),
                listOf(
                    "a" inc 1 If ("b" lt 4),
                    "b" inc 5 If ("a" le 1),
                )
            ),
            Arguments.of(
                Registers.registersOf("a" to 1, "b" to 5, "c" to 123),
                listOf(
                    "a" inc 1 If ("b" lt 4),
                    "b" inc 5 If ("a" ge 1),
                    "c" inc 100 If ("a" eq 1),
                    "c" inc 23 If ("b" neq 0),
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

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderEmulatorMaxRegister::class)
    fun `test findLargestRegisterValueAfterEmulation`(expectedValue: Int, instructions: List<Instruction>) =
        with(ConditionalArithInstructionsEmulator()) {
            assertEquals(expectedValue, findLargestRegisterValueAfterEmulation(instructions))
        }

    private class ArgumentsProviderEmulatorMaxRegister : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                1,
                listOf(
                    "b" inc 5 If ("a" gt 1),
                    "a" inc 1 If ("b" lt 4),
                )
            ),
            Arguments.of(
                5,
                listOf(
                    "a" inc 1 If ("b" lt 4),
                    "b" inc 5 If ("a" ge 1),
                )
            ),
            Arguments.of(
                1,
                listOf(
                    "b" inc 5 If ("a" gt 1),
                    "a" inc 1 If ("b" lt 4),
                    "c" dec -10 If ("a" ge 1),
                    "c" inc -20 If ("c" eq 10),
                )
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderEmulatorMaxRegisterDuringEmulation::class)
    fun `test findLargestRegisterValueDuringEmulation`(expectedValue: Int, instructions: List<Instruction>) =
        with(ConditionalArithInstructionsEmulator()) {
            assertEquals(expectedValue, findLargestRegisterValueDuringEmulation(instructions))
        }

    private class ArgumentsProviderEmulatorMaxRegisterDuringEmulation : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                1,
                listOf(
                    "b" inc 5 If ("a" gt 1),
                    "a" inc 1 If ("b" lt 4),
                )
            ),
            Arguments.of(
                5,
                listOf(
                    "a" inc 1 If ("b" lt 4),
                    "b" inc 5 If ("a" ge 1),
                )
            ),
            Arguments.of(
                10,
                listOf(
                    "b" inc 5 If ("a" gt 1),
                    "a" inc 1 If ("b" lt 4),
                    "c" dec -10 If ("a" ge 1),
                    "c" inc -20 If ("c" eq 10),
                )
            ),
        )
    }
}
