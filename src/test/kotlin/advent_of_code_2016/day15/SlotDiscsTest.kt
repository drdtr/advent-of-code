package advent_of_code_2016.day15

import advent_of_code_2016.day14.OneTimePadTest.*
import advent_of_code_2016.day15.SlotDiscs.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class SlotDiscsTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSlotDiscs::class)
    fun `test SlotDiscs`(expected: Int, discs: Iterable<Disc>) = with(SlotDiscs()) {
        assertEquals(expected, findEarliestTimeToDropCapsule(discs))
    }

    private class ArgumentsProviderSlotDiscs : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(
                5, listOf(
                    Disc(timeOffset = 1, numPositions = 5, initialPosition = 4),
                    Disc(timeOffset = 2, numPositions = 2, initialPosition = 1),
                )
            ),
        )
    }
}