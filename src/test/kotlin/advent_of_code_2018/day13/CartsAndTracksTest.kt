package advent_of_code_2018.day13

import advent_of_code_2018.day13.CartsAndTracks.*
import advent_of_code_2018.day13.CartsAndTracksTest.ArgumentsProviders.ArgumentsProviderCartsAndTracks
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class CartsAndTracksTest {

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCartsAndTracks::class)
    fun `test findLocationOfFirstCollision`(expected: Point, trackGridWithCarts: List<String>) = with(CartsAndTracks()) {
        assertEquals(expected, findLocationOfFirstCollision(trackGridWithCarts))
    }

    private object ArgumentsProviders {
        val gridWithCarts = listOf(
            """/->-\        """,
            """|   |  /----\""",
            """| /-+--+-\  |""",
            """| | |  | v  |""",
            """\-+-/  \-+--/""",
            """  \------/   """,
        )

        class ArgumentsProviderCartsAndTracks : ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?) = Stream.of(
                arguments(Point(7, 3), gridWithCarts),
            )
        }
    }
}