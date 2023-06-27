package advent_of_code_2015.day23

import advent_of_code_2015.day23.ProcessorSimulator.*
import advent_of_code_2015.day23.ProcessorSimulator.Registers.*
import advent_of_code_2015.day23.ProcessorSimulator.Registers.Companion.registersOf
import org.junit.Assume.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.runners.Parameterized.*
import java.util.stream.Stream

internal class ProcessorSimulatorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderProcessorSimulator::class)
    fun `test ProcessorSimulator`(expected: Registers, initRegisters: Registers, instructions: List<Instruction>) =
        with(ProcessorSimulator()) {
            assertEquals(expected, simulate(instructions, initRegisters))
        }

    private class ArgumentsProviderProcessorSimulator : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                registersOf("a" to 2),
                registersOf(),
                listOf(
                    Increment("a"),
                    JumpIfOne("a", 2),
                    Tripple("a"),
                    Increment("a"),
                )
            ),
            Arguments.of(
                registersOf("a" to 2, "b" to 3),
                registersOf(),
                listOf(
                    Increment("a"),
                    JumpIfOne("a", 2),
                    Tripple("a"),
                    Increment("a"),
                    Increment("b"),
                    Tripple("b"),
                    JumpIfEven("b", +2),
                    Jump(100),
                    JumpIfOne("a", 2),
                    Jump(200),
                    Jump(-10),
                )
            ),
            Arguments.of(
                registersOf("a" to 2, "b" to 3),
                registersOf("a" to 1),
                listOf(
                    JumpIfOne("a", 2),
                    Tripple("a"),
                    Increment("a"),
                    Increment("b"),
                    Tripple("b"),
                    JumpIfEven("b", +2),
                    Jump(100),
                    JumpIfOne("a", 2),
                    Jump(200),
                    Jump(-10),
                )
            ),
        ).map {
            // workaround for `value class` not being handled correctly in JUnit
            Arguments.of(
                (it.get()[0] as Registers).registers,
                (it.get()[1] as Registers).registers,
                it.get()[2]
            )
        }
    }
}