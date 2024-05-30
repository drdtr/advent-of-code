package advent_of_code_2017.day20

import advent_of_code_2017.day20.Cooridnates.Coordinate3D
import advent_of_code_2017.day20.Cooridnates.Particle3D
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.*
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class CoordinateDistanceTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderCountParticlesRemainingAfterCollisions::class)
    fun `test countParticlesRemainingAfterCollisions`(expected: Int, particles: List<Particle3D<Int>>) =
        with(CoordinateDistance()) {
            assertEquals(expected, countParticlesRemainingAfterCollisions(particles))
        }

    private class ArgumentsProviderCountParticlesRemainingAfterCollisions : ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?) = Stream.of(
            arguments(
                1, listOf(
                    Particle3D(
                        id = 1,
                        p = Coordinate3D(-6, 0, 0),
                        v = Coordinate3D(3, 0, 0),
                        a = Coordinate3D(0, 0, 0),
                    ),
                    Particle3D(
                        id = 2,
                        p = Coordinate3D(-4, 0, 0),
                        v = Coordinate3D(2, 0, 0),
                        a = Coordinate3D(0, 0, 0),
                    ),
                    Particle3D(
                        id = 3,
                        p = Coordinate3D(-2, 0, 0),
                        v = Coordinate3D(1, 0, 0),
                        a = Coordinate3D(0, 0, 0),
                    ),
                    Particle3D(
                        id = 4,
                        p = Coordinate3D(3, 0, 0),
                        v = Coordinate3D(-1, 0, 0),
                        a = Coordinate3D(0, 0, 0),
                    ),
                )
            ),
            arguments(
                1, listOf(
                    Particle3D(
                        id = 1,
                        p = Coordinate3D(21, 22, 23),
                        v = Coordinate3D(1, 1, 1),
                        a = Coordinate3D(0, 0, 0),
                    ),
                    Particle3D(
                        id = 2,
                        p = Coordinate3D(20, 20, 20),
                        v = Coordinate3D(1, 1, 1),
                        a = Coordinate3D(1, 2, 3),
                    ),
                    Particle3D(
                        id = 3,
                        p = Coordinate3D(1, 2, 3),
                        v = Coordinate3D(11, 11, 11),
                        a = Coordinate3D(0, 0, 0),
                    ),
                )
            ),
        )
    }
}