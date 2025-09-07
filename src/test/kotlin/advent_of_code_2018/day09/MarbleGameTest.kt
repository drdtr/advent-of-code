package advent_of_code_2018.day09

import advent_of_code_2018.day09.MarbleGameTest.ArgumentsProviders.ArgumentsProviderMarbleGameWinningScore
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Named.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class MarbleGameTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderMarbleGameWinningScore::class)
    fun `test calcMarbleGameWinningScore`(expected: Int, numOfPlayers: Int, maxMarbleValue: Int) = with(MarbleGame()) {
        assertEquals(expected, calcMarbleGameWinningScore(numOfPlayers, maxMarbleValue))
    }

    private object ArgumentsProviders {
        class ArgumentsProviderMarbleGameWinningScore : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(named("expected", 32), named("numOfPlayers", 10), named("maxMarbleValue", 25)),
                arguments(named("expected", 8317), named("numOfPlayers", 10), named("maxMarbleValue", 1618)),
                arguments(named("expected", 146373), named("numOfPlayers", 13), named("maxMarbleValue", 7999)),
                arguments(named("expected", 2764), named("numOfPlayers", 17), named("maxMarbleValue", 1104)),
                arguments(named("expected", 54718), named("numOfPlayers", 21), named("maxMarbleValue", 6111)),
                arguments(named("expected", 37305), named("numOfPlayers", 30), named("maxMarbleValue", 5807)),
            )
        }
    }
}