package advent_of_code_2018.day12

import advent_of_code_2018.day12.ProductionRulesEmulator.*
import advent_of_code_2018.day12.ProductionRulesEmulatorTest.ArgumentsProviders.ArgumentsProviderEmulateProductionRules
import advent_of_code_2018.day12.ProductionRulesEmulatorTest.ArgumentsProviders.ArgumentsProviderSumOfFilledCellPositions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class ProductionRulesEmulatorTest {
    @ParameterizedTest(name = "{index}: from {0} expected {1} with offset {2} ")
    @ArgumentsSource(ArgumentsProviderEmulateProductionRules::class)
    fun `test EmulatorBruteForce`(
        initialState: String,
        expected: String,
        expectedOffset: Int,
        productionRules: List<ProductionRule>,
    ) = with(EmulatorBruteForce(initialState, productionRules)) {
        emulate()
        assertEquals(expected, currentState.cells)
        assertEquals(expectedOffset, currentState.offset)
    }

    @ParameterizedTest(name = "{index}: from {0} expected {1} with offset {2} ")
    @ArgumentsSource(ArgumentsProviderEmulateProductionRules::class)
    fun `test EmulatorWithTrie`(
        initialState: String,
        expected: String,
        expectedOffset: Int,
        productionRules: List<ProductionRule>,
    ) = with(EmulatorWithTrie(initialState, productionRules)) {
        emulate()
        assertEquals(expected, currentState.cells)
        assertEquals(expectedOffset, currentState.offset)
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSumOfFilledCellPositions::class)
    fun `test sumOfFilledCellPositions`(
        initialState: String,
        numOfCycles: Long,
        expected: Int,
        productionRules: List<ProductionRule>,
    ) = with(ProductionRulesEmulator()) {
        assertEquals(expected, sumOfFilledCellPositions(initialState, productionRules, numOfCycles))
    }

    private object ArgumentsProviders {
        val productionRules = listOf(
            ProductionRule("...##", '#'),
            ProductionRule("..#..", '#'),
            ProductionRule(".#...", '#'),
            ProductionRule(".#.#.", '#'),
            ProductionRule(".#.##", '#'),
            ProductionRule(".##..", '#'),
            ProductionRule(".####", '#'),
            ProductionRule("#.#.#", '#'),
            ProductionRule("#.###", '#'),
            ProductionRule("##.#.", '#'),
            ProductionRule("##.##", '#'),
            ProductionRule("###..", '#'),
            ProductionRule("###.#", '#'),
            ProductionRule("####.", '#'),
        )

        class ArgumentsProviderEmulateProductionRules : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                // After 1 cycle
                arguments(
                    "#..#.#..##......###...###", // initialState
                    "#...#....#.....#..#..#..#", // expected
                    0, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 2 cycles
                arguments(
                    "#...#....#.....#..#..#..#", // initialState
                    "##..##...##....#..#..#..##", // expected
                    0, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 3 cycles
                arguments(
                    "##..##...##....#..#..#..##", // initialState
                    "#.#...#..#.#....#..#..#...#", // expected
                    -1, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 4 cycles
                arguments(
                    "#.#...#..#.#....#..#..#...#", // initialState
                    "#.#..#...#.#...#..#..##..##", // expected
                    1, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 5 cycles
                arguments(
                    "#.#..#...#.#...#..#..##..##", // initialState
                    "#...##...#.#..#..#...#...#", // expected
                    1, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 6 cycles
                arguments(
                    "#...##...#.#..#..#...#...#", // initialState
                    "##.#.#....#...#..##..##..##", // expected
                    0, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 7 cycles
                arguments(
                    "##.#.#....#...#..##..##..##", // initialState
                    "#..###.#...##..#...#...#...#", // expected
                    -1, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 8 cycles
                arguments(
                    "#..###.#...##..#...#...#...#", // initialState
                    "#....##.#.#.#..##..##..##..##", // expected
                    0, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 9 cycles
                arguments(
                    "#....##.#.#.#..##..##..##..##", // initialState
                    "##..#..#####....#...#...#...#", // expected
                    0, // expectedOffset
                    productionRules, // productionRules
                ),
                // After 10 cycles
                arguments(
                    "##..#..#####....#...#...#...#", // initialState
                    "#.#..#...#.##....##..##..##..##", // expected
                    -1, // expectedOffset
                    productionRules, // productionRules
                ),
            )
        }

        class ArgumentsProviderSumOfFilledCellPositions : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(
                    "#..#.#..##......###...###", // initialState
                    20, // numOfCycles
                    325, // expected
                    productionRules, // productionRules
                ),
            )
        }
    }
}
