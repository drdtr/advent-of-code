package advent_of_code_2016.day04

import advent_of_code_2016.day01.StreetGridDistance.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class RoomNameDecryptorTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderSumOfSectorIDsOfValidRooms::class)
    fun `test sumOfSectorIDsOfValidRooms`(expected: Int, roomStrings: List<String>) = with(RoomNameDecryptor()) {
        assertEquals(expected, sumOfSectorIDsOfValidRooms(roomStrings))
    }

    private class ArgumentsProviderSumOfSectorIDsOfValidRooms : ArgumentsProvider {
        private val room_123_valid = "aaaaa-bbb-z-y-x-123[abxyz]"
        private val room_987_valid = "a-b-c-d-e-f-g-h-987[abcde]"
        private val room_404_valid = "not-a-real-room-404[oarel]"
        private val room_200_invalid = "totally-real-room-200[decoy]"

        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(123, listOf(room_123_valid)),
            Arguments.of(987, listOf(room_987_valid)),
            Arguments.of(404, listOf(room_404_valid)),
            Arguments.of(0, listOf(room_200_invalid)),

            Arguments.of(123, listOf(room_123_valid, room_200_invalid)),
            Arguments.of(987, listOf(room_200_invalid, room_987_valid)),
            Arguments.of(404, listOf(room_404_valid, room_200_invalid)),

            Arguments.of(1110, listOf(room_123_valid, room_200_invalid, room_987_valid)),
            Arguments.of(1391, listOf(room_200_invalid, room_987_valid, room_404_valid)),
            Arguments.of(1514, listOf(room_123_valid, room_200_invalid, room_987_valid, room_404_valid)),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderFindRoomWhereDecryptedNameContains::class)
    fun `test findRoomWhereDecryptedNameContains`(expected: Int?, searchText: String, roomStrings: List<String>) =
        with(RoomNameDecryptor()) {
            assertEquals(expected, findRoomWhereDecryptedNameContains(searchText, roomStrings)?.sectorId)
        }

    private class ArgumentsProviderFindRoomWhereDecryptedNameContains : ArgumentsProvider {
        private val roomValid = "qzmt-zixmtkozy-ivhz-343[zimth]"
        private val roomInvalid = "qzmt-zixmtkozy-ivhz-343[abcde]"

        override fun provideArguments(context: ExtensionContext?) = Stream.of(
            Arguments.of(null, "notFound", listOf(roomValid)),
            Arguments.of(null, "very encrypted", listOf(roomInvalid)),
            Arguments.of(343, "very encrypted", listOf(roomValid)),
        )
    }
}