package advent_of_code_2016.day06

import advent_of_code_2016.day06.RepetitionCoding.CharacterRestoringMode.*
import advent_of_code_2016.day06.RepetitionCodingTest.ArgumentsProviders.ArgumentsProviderRestoreUsingRepetitionCodeWithLeastFrequentCharacter
import advent_of_code_2016.day06.RepetitionCodingTest.ArgumentsProviders.ArgumentsProviderRestoreUsingRepetitionCodeWithMostFrequentCharacter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class RepetitionCodingTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderRestoreUsingRepetitionCodeWithMostFrequentCharacter::class)
    fun `test restoreUsingRepetitionCode with MostFrequentCharacter`(expected: String, messages: List<String>) =
        with(RepetitionCoding()) {
            assertEquals(expected, restoreUsingRepetitionCode(messages, MostFrequentCharacter))
        }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderRestoreUsingRepetitionCodeWithLeastFrequentCharacter::class)
    fun `test restoreUsingRepetitionCode with LeastFrequentCharacter`(expected: String, messages: List<String>) =
        with(RepetitionCoding()) {
            assertEquals(expected, restoreUsingRepetitionCode(messages, LeastFrequentCharacter))
        }

    private object ArgumentsProviders {
        val messageList1 = listOf(
            "eedadn",
            "drvtee",
            "eandsr",
            "raavrd",
            "atevrs",
            "tsrnev",
            "sdttsa",
            "rasrtv",
            "nssdts",
            "ntnada",
            "svetve",
            "tesnvt",
            "vntsnd",
            "vrdear",
            "dvrsen",
            "enarar",
        )

        class ArgumentsProviderRestoreUsingRepetitionCodeWithMostFrequentCharacter : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?) =
                Stream.of(Arguments.of("easter", messageList1))
        }

        class ArgumentsProviderRestoreUsingRepetitionCodeWithLeastFrequentCharacter : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?) =
                Stream.of(Arguments.of("advent", messageList1))
        }
    }
}