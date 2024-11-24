package advent_of_code_2017.day24

import advent_of_code_2017.day24.TwoSidedConnectors.Connector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class MaxPathWeightWithTwoSidedConnectorsPart1Test {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMaxPathWeightWithTwoSidedConnectors::class)
    fun `test findMaxPathWeight`(expected: Int, connectors: List<Connector>) =
        with(MaxPathWeightWithTwoSidedConnectorsPart1()) {
            assertEquals(expected, findMaxPathWeight(connectors))
        }

    private class ArgumentsProviderMaxPathWeightWithTwoSidedConnectors : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            Arguments.arguments(
                2, listOf(
                    Connector(0, 2),
                )
            ),
            Arguments.arguments(
                6, listOf(
                    Connector(0, 2),
                    Connector(0, 3),
                    Connector(2, 2),
                )
            ),
            Arguments.arguments(
                11, listOf(
                    Connector(0, 2),
                    Connector(2, 2),
                    Connector(2, 3),
                    Connector(4, 6),
                )
            ),
            Arguments.arguments(
                12, listOf(
                    Connector(0, 2),
                    Connector(2, 2),
                    Connector(2, 3),
                    Connector(4, 0),
                    Connector(4, 4),
                )
            ),
            Arguments.arguments(
                31, listOf(
                    Connector(0, 2),
                    Connector(2, 2),
                    Connector(2, 3),
                    Connector(3, 4),
                    Connector(3, 5),
                    Connector(0, 1),
                    Connector(10, 1),
                    Connector(9, 10),
                )
            ),
        )
    }
}