package advent_of_code_2017.day07

import advent_of_code_2017.day07.TowerGraph.*
import advent_of_code_2017.day07.TowerGraphTest.ArgumentsProviderTowerGraph.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class TowerGraphTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderTowerGraphBottomNode::class)
    fun `test findBottomNode`(expected: String, nodes: List<Node>) = with(TowerGraph()) {
        assertEquals(expected, findBottomNode(nodes).name)
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderTowerGraphCorrectedNodeWeight::class)
    fun `test calcCorrectedNodeWeight`(expected: NodeWeight, nodes: List<Node>) = with(TowerGraph()) {
        assertEquals(expected, calcCorrectedNodeWeight(nodes))
    }

    private interface ArgumentsProviderTowerGraph {
        companion object {
            val towerNodes1 = listOf(
                Node("N2.1", 66),
                Node("N3.3", 57),
                Node("N1.2", 61),
                Node("N2.2", 66),
                Node("N3.1", 57),
                Node("N3", 72, setOf("N3.1", "N3.2", "N3.3")),
                Node("N2.3", 66),
                Node("N2", 45, setOf("N2.1", "N2.2", "N2.3")),
                Node("B", 41, setOf("N1", "N2", "N3")),
                Node("N1.3", 61),
                Node("N1", 68, setOf("N1.1", "N1.2", "N1.3")),
                Node("N1.1", 61),
                Node("N3.2", 57),
            )
        }

        class ArgumentsProviderTowerGraphBottomNode : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
                Stream.of(Arguments.of("B", towerNodes1))
        }

        class ArgumentsProviderTowerGraphCorrectedNodeWeight : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?) =
                Stream.of(Arguments.of(NodeWeight("N1", 60), towerNodes1))
        }
    }
}