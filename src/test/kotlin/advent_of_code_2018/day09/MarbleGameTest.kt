package advent_of_code_2018.day09

import advent_of_code_2018.day09.MarbleGameTest.ArgumentsProviders.ArgumentsProviderMarbleGameWinningScore
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class MarbleGameTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMarbleGameWinningScore::class)
    fun `test calcMarbleGameWinningScore`(expected: Long, numOfPlayers: Int, maxMarbleValue: Int) = with(MarbleGame()) {
        assertEquals(expected, calcMarbleGameWinningScore(numOfPlayers, maxMarbleValue))
    }

    private object ArgumentsProviders {
        class ArgumentsProviderMarbleGameWinningScore : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(32, 10, 25),
                arguments(8317, 10, 1618),
                arguments(146373, 13, 7999),
                arguments(2764, 17, 1104),
                arguments(54718, 21, 6111),
                arguments(37305, 30, 5807),
            )
        }
    }
}