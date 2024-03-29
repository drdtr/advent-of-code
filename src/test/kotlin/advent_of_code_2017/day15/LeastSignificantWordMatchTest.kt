package advent_of_code_2017.day15

import advent_of_code_2017.day15.LeastSignificantWordMatch.*
import advent_of_code_2017.day15.LeastSignificantWordMatchTest.ArgumentsProviderLeastSignificantWordMatch.Companion.generatorA
import advent_of_code_2017.day15.LeastSignificantWordMatchTest.ArgumentsProviderLeastSignificantWordMatch.Companion.generatorB
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class LeastSignificantWordMatchTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderLeastSignificantWordMatchCount::class)
    fun `test countEqualLSWs`(expected: Int, generatorA: Generator, generatorB: Generator, numRounds: Int) =
        with(LeastSignificantWordMatch()) {
            assertEquals(expected, countEqualLSWs(generatorA, generatorB, numRounds))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderLeastSignificantWordMatchCountWithModulo::class)
    fun `test countEqualLSWsMatchingGeneratorsModulo`(
        expected: Int,
        generatorA: Generator,
        generatorB: Generator,
        numRounds: Int
    ) =
        with(LeastSignificantWordMatch()) {
            assertEquals(expected, countEqualLSWsMatchingGeneratorsModulo(generatorA, generatorB, numRounds))
        }

    private interface ArgumentsProviderLeastSignificantWordMatch : ArgumentsProvider {
        companion object {
            val generatorA = Generator(initValue = 65, factor = 16807, modulo = 4)
            val generatorB = Generator(initValue = 8921, factor = 48271, modulo = 8)
        }
    }

    private class ArgumentsProviderLeastSignificantWordMatchCount : ArgumentsProviderLeastSignificantWordMatch {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, generatorA, generatorB, 1),
            Arguments.of(0, generatorA, generatorB, 2),
            Arguments.of(1, generatorA, generatorB, 3),
            Arguments.of(1, generatorA, generatorB, 4),
            Arguments.of(588, generatorA, generatorB, 40_000_000),
        )
    }

    private class ArgumentsProviderLeastSignificantWordMatchCountWithModulo :
        ArgumentsProviderLeastSignificantWordMatch {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(0, generatorA, generatorB, 1),
            Arguments.of(0, generatorA, generatorB, 2),
            Arguments.of(0, generatorA, generatorB, 1055),
            Arguments.of(1, generatorA, generatorB, 1056),
            Arguments.of(1, generatorA, generatorB, 1057),
            Arguments.of(309, generatorA, generatorB, 5_000_000),
        )
    }

}