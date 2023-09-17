package advent_of_code_2016.day10

import advent_of_code_2016.day10.BotSimulator.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertNotNull

internal class BotSimulatorTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderBotSimulator::class)
    fun `test BotSimulator`(
        expectedBotPerformingComparisons: List<Pair<Comparison, Int>>,
        expectedValuesForSelectedOutputs: List<Pair<Int, Int>>,
        ops: List<BotOp>
    ) =
        with(BotSimulator()) {
            val simulationState = simulate(ops)

            for ((comparisonToFind, expectedBotNumber) in expectedBotPerformingComparisons) {
                assertEquals(
                    expectedBotNumber,
                    findBotNumberThatPerformedComparison(simulationState, comparisonToFind)
                )
            }
            for ((outputNumber, expectedValue) in expectedValuesForSelectedOutputs) {
                assertEquals(expectedValue, assertNotNull(simulationState.outputs[outputNumber]).value)
            }
        }

    private class ArgumentsProviderBotSimulator : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                listOf(Comparison(low = 2, high = 5) to 2),
                listOf(0 to 5, 1 to 2, 2 to 3),
                listOf(
                    InputOp(value = 5, botNumber = 2),
                    BotSendOp(botNumber = 2, lowSendTarget = BotTarget(1), highSendTarget = BotTarget(0)),
                    InputOp(value = 3, botNumber = 1),
                    BotSendOp(botNumber = 1, lowSendTarget = OutputTarget(1), highSendTarget = BotTarget(0)),
                    BotSendOp(botNumber = 0, lowSendTarget = OutputTarget(2), highSendTarget = OutputTarget(0)),
                    InputOp(value = 2, botNumber = 2),
                )
            ),
        )
    }

}