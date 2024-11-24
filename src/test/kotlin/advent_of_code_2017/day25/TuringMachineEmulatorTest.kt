package advent_of_code_2017.day25

import advent_of_code_2017.day25.TuringMachine.*
import advent_of_code_2017.day25.TuringMachine.CursorStep.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class TuringMachineEmulatorTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMaxPathWeightWithTwoSidedConnectors::class)
    fun `test findMaxPathWeight`(expected: Int, turingMachine: TuringMachine, initState: State, numSteps: Int) =
        with(TuringMachineEmulator()) {
            assertEquals(expected, emulateAndGetChecksum(turingMachine, initState, numSteps))
        }

    private class ArgumentsProviderMaxPathWeightWithTwoSidedConnectors : ArgumentsProvider {
        private val stateA = State("A")
        private val stateB = State("B")
        private val turingMachine1 = TuringMachine(
            possibleTransitions = listOf(
                Transition(stateA, 0, stateB, 1, Right),
                Transition(stateA, 1, stateB, 0, Left),
                Transition(stateB, 0, stateA, 1, Left),
                Transition(stateB, 1, stateA, 1, Right),
            )
        )

        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            Arguments.arguments(3, turingMachine1, stateA, 6),
        )
    }
}