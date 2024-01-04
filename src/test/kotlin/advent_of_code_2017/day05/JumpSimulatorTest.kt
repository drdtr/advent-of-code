package advent_of_code_2017.day05

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class JumpSimulatorTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderJumpSimulator::class)
    fun `test countSteps`(expected: Int, jumpDistances: List<Int>) = with(JumpSimulator()) {
        assertEquals(expected, countSteps(jumpDistances))
    }

    private class ArgumentsProviderJumpSimulator : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(5, listOf(0, 3, 0, 1, -3)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderJumpSimulatorDecreaseDistanceIfGeqThree::class)
    fun `test countStepsDecreaseDistanceIfGeqThree`(expected: Int, jumpDistances: List<Int>) = with(JumpSimulator()) {
        assertEquals(expected, countStepsDecreaseDistanceIfGeqThree(jumpDistances))
    }

    private class ArgumentsProviderJumpSimulatorDecreaseDistanceIfGeqThree : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(10, listOf(0, 3, 0, 1, -3)),
        )
    }
}