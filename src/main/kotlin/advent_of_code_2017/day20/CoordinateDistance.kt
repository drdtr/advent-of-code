package advent_of_code_2017.day20

import Util.readInputLines
import advent_of_code_2017.day20.Cooridnates.Coordinate3D
import advent_of_code_2017.day20.Cooridnates.Particle3D
import advent_of_code_2017.day20.Cooridnates.manhattanDistance
import java.util.regex.Pattern
import kotlin.math.abs

/**
 * [Particle Swarm](https://adventofcode.com/2017/day/20)
 *
 * You're given a set of particles `p<x, y, z>` in 3-D space, where each particle `pi` has
 * - initial position `p<xi, yi, zi>`
 * - initial speed `v<xi, yi, zi>`
 * - acceleration `a<xi, yi, zi>`.
 *
 * Each time point `t > 0`, the speed `vi` is modified by `ai` and the position is modified by the new speed.
 *
 * ### Part 1
 *
 * Using Manhattan distance, determine which particle `pi` will stay closest to the coordinate origin `<0, 0, 0>`
 * in the long run, i.e. such that there exists a `t0` such that for all `t >= t0`:
 * `|pxi| + |pyi| + |pzi| >= |pxj| + |pyj| + |pzj|` for all `j`.
 *
 * ### Part 2
 *
 *
 *
 */
class CoordinateDistance {
    fun findParticleStayingClosestToCoordinateOrigin(particles: List<Particle3D<Int>>): Particle3D<Int> {
        val minAcceleration = particles.minOf { it.a.manhattanDistance() }
        val particlesWithMinAcceleration = particles.filter { it.a.manhattanDistance() == minAcceleration }
//        println("Particles with minimum acceleration:\n" + particlesWithMinAcceleration.joinToString(separator = "\n"))
        if (particlesWithMinAcceleration.size == 1) return particlesWithMinAcceleration.first()

        val minVelocity = particlesWithMinAcceleration.minOf { it.v.manhattanDistance() }
        val particlesWithMinVelocity = particlesWithMinAcceleration.filter { it.v.manhattanDistance() == minVelocity }
//        println("Particles with minimum velocity:\n" + particlesWithMinVelocity.joinToString(separator = "\n"))
        if (particlesWithMinVelocity.size == 1) return particlesWithMinVelocity.first()

        /*
        We didn't encounter a case with multiple particles having both minimal acceleration and velocity,
        so we didn't implement it for now. If needed, a possible solution idea could be to "normalize" the acceleration
        so that it's always positive by changing the signs for acceleration, velocity, and initial position
        where the acceleration was negative. This is alright, because we're only interested in the distance
        to <0,0,0> and no in the direction of the particle. Then we can just calculate the position
        of all particles at a sufficiently large time `t` so that all positions become positive -
        then the particle with the smallest distance will remain closest.
        In the case that the acceleration is 0, we can still normalize the velocity,
        and in the case that even the velocity is 0, then the distance is constant, so we'll just need to
        find out which of the particles with velocity acceleration and velocity 0 is closest to <0, 0, 0>.
         */
        TODO(
            "Not implemented for the case when multiple particles have both minimal acceleration and velocity, " +
                    "because this case wasn't encountered in the input. " +
                    "See code comment above for a solution idea for this case"
        )
    }
}

object Cooridnates {
    data class Coordinate3D<T : Number>(val x: T, val y: T, val z: T)
    data class Particle3D<T : Number>(
        val id: Int,
        val p: Coordinate3D<T>,
        val v: Coordinate3D<T>,
        val a: Coordinate3D<T>,
    )

    fun Coordinate3D<Int>.manhattanDistance() = abs(x) + abs(y) + abs(z)
}

private val intPatternStr = "-?\\d+"
private val coordinate3dPattern = Pattern.compile("($intPatternStr),($intPatternStr),($intPatternStr)")
private val particleInputMatcher =
    Pattern.compile("p=<\\s*($coordinate3dPattern)>, v=<\\s*($coordinate3dPattern)>, a=<\\s*($coordinate3dPattern)>")


private fun parseCoordinate3d(s: String): Coordinate3D<Int> {
    with(coordinate3dPattern.matcher(s)) {
        if (matches()) return Coordinate3D(group(1).toInt(), group(2).toInt(), group(3).toInt())
    }
    error("Invalid 3D coordinates: $s")
}

private fun parseParticle(id: Int, s: String): Particle3D<Int> {
    with(particleInputMatcher.matcher(s)) {
        if (matches()) return Particle3D(
            id,
            p = parseCoordinate3d(group(1)),
            v = parseCoordinate3d(group(5)),
            a = parseCoordinate3d(group(9)),
        )
    }
    error("Invalid particle $id: $s")
}

private fun readInput(inputFileName: String): List<Particle3D<Int>> =
    readInputLines(2017, 20, inputFileName).mapIndexed { index, line -> parseParticle(index, line) }

private fun printResult(inputFileName: String) {
    val particles = readInput(inputFileName)
    val solver = CoordinateDistance()

    // Part 1
    val res1 = solver.findParticleStayingClosestToCoordinateOrigin(particles)
    println("Particle that will stay closest to <0, 0, 0> in the long run: $res1")
}

fun main() {
    printResult("input.txt")
}
