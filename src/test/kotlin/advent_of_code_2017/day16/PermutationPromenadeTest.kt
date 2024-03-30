package advent_of_code_2017.day16

import advent_of_code_2017.day16.PermutationPromenade.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class PermutationPromenadeTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderPermutationPromenadePermute::class)
    fun `test permute`(expected: String, elements: String, ops: List<PermutationOp>) = with(PermutationPromenade()) {
        assertEquals(expected, permute(elements, ops))
    }

    private class ArgumentsProviderPermutationPromenadePermute : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("eabcd", "abcde", listOf(Spin(1))),
            Arguments.of("deabc", "abcde", listOf(Spin(2))),
            Arguments.of("cdeab", "abcde", listOf(Spin(3))),
            Arguments.of("bcdea", "abcde", listOf(Spin(4))),
            Arguments.of("abcde", "abcde", listOf(Spin(5))),
            Arguments.of("eabcd", "abcde", listOf(Spin(6))),
            Arguments.of("eabdc", "abcde", listOf(Spin(1), Exchange(3, 4))),
            Arguments.of("baedc", "abcde", listOf(Spin(1), Exchange(3, 4), Partner('e', 'b'))),
            Arguments.of("abced", "abcde", listOf(Exchange(3, 4))),
            Arguments.of("abedc", "abcde", listOf(Exchange(2, 4))),
            Arguments.of("adcbe", "abcde", listOf(Exchange(1, 3))),
            Arguments.of(
                "ceadb", "abcde", listOf(
                    Spin(1), Exchange(3, 4), Partner('e', 'b'),
                    Spin(1), Exchange(3, 4), Partner('e', 'b'),
                )
            ),
            Arguments.of(
                "ecbda", "abcde", listOf(
                    Spin(1), Exchange(3, 4), Partner('e', 'b'),
                    Spin(1), Exchange(3, 4), Partner('e', 'b'),
                    Spin(1), Exchange(3, 4), Partner('e', 'b'),
                )
            ),
            Arguments.of(
                "ceadb",
                "abcde",
                List(30) { listOf(Spin(1), Exchange(3, 4), Partner('e', 'b')) }.flatten(),
            ),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderPermutationPromenadePermuteRepeated::class)
    fun `test permuteRepeatedWithCycleDetection`(
        expected: String,
        elements: String,
        ops: List<PermutationOp>,
        numOfReps: Int,
    ) =
        with(PermutationPromenade()) {
            assertEquals(expected, permuteRepeatedWithCycleDetection(elements, ops, numOfReps))
        }

    private class ArgumentsProviderPermutationPromenadePermuteRepeated : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("eabcd", "abcde", listOf(Spin(1)), 1),
            Arguments.of("deabc", "abcde", listOf(Spin(1)), 2),
            Arguments.of("cdeab", "abcde", listOf(Spin(1)), 3),
            Arguments.of("bcdea", "abcde", listOf(Spin(1)), 4),
            Arguments.of("abcde", "abcde", listOf(Spin(1)), 5),
            Arguments.of("ceadb", "abcde", listOf(Spin(1), Exchange(3, 4), Partner('e', 'b')), 2),
            Arguments.of("ecbda", "abcde", listOf(Spin(1), Exchange(3, 4), Partner('e', 'b')), 3),
            Arguments.of("ceadb", "abcde", listOf(Spin(1), Exchange(3, 4), Partner('e', 'b')), 30),
        )
    }

}