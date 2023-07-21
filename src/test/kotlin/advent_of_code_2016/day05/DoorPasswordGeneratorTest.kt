package advent_of_code_2016.day05

import advent_of_code_2016.day04.RoomNameDecryptorTest.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class DoorPasswordGeneratorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGenerateDoorPassword::class)
    fun `test generateDoorPassword`(expected: String, doorId: String) = with(DoorPasswordGenerator()) {
        assertEquals(expected, generateDoorPassword(doorId, 8))
    }

    private class ArgumentsProviderGenerateDoorPassword : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("18f47a30", "abc"),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGenerateDoorPasswordWithPosition::class)
    fun `test generateDoorPasswordWithPosition`(expected: String, doorId: String) = with(DoorPasswordGenerator()) {
        assertEquals(expected, generateDoorPasswordWithPosition(doorId, 8))
    }

    private class ArgumentsProviderGenerateDoorPasswordWithPosition : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of("05ace8e3", "abc"),
        )
    }
}