package advent_of_code_2017.day20

import Util.readInputLines
import advent_of_code_2017.day20.Cooridnates.Coordinate3D
import advent_of_code_2017.day20.Cooridnates.Particle3D
import advent_of_code_2017.day20.Cooridnates.manhattanDistance
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

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
 * Particles that collide, are destroyed. Calculate how many particles are left after all collisions are resolved.
 *
 * #### Solution idea.
 *
 * The coordinates c \in {x, y, z} of a particle `p` at time point `t` are:
 * ```
 * c(t) = ci(0) + vc * t + ac * t * (t + 1) / 2
 * ```
 *
 * Two particles `i` and `j` collide if `ci(t) = cj(t)` for some `t >= 0` for all `c in {x, y, z}`
 * and they did not collide with another particle at an earlier time point.
 *
 * The potential collision time for particles `i` and `j` are:
 * ```
 * ci(0) + vci * t + aci * t * (t + 1) / 2 = cj(0) + vcj * t + acj * t * (t + 1) / 2
 * (aci - acj) * t * (t + 1) / 2 + (vci - vcj) * t + (ci(0) - cj(0)) = 0
 * (aci - acj) * t^2 / 2 + (aci - acj) * t / 2 + (vci - vcj) * t + (ci(0) - cj(0)) = 0
 * (aci - acj) * t^2 + (aci - acj) * t + 2 * (vci - vcj) * t + 2 * (ci(0) - cj(0)) = 0
 * (aci - acj) * t^2 + (aci - acj + 2 * (vci - vcj)) * t + 2 * (ci(0) - cj(0)) = 0
 * ```
 * For brevity, let's denote the terms as
 * ```
 * A = aci - acj
 * B = aci - acj + 2 * (vci - vcj) = A + 2 * (vci - vcj)
 * C = 2 * (ci(0) - cj(0))
 * ```
 * So that the equation is
 * ```
 * A * t^2 + B * t + C
 * ```
 * The solution for the quadratic equation is:
 * ```
 * D = B^2 - 4AC
 * t1,2 = (-B +- Sqrt(D)) / 2A
 * ```
 *
 * If however, `A = 0` then the equation is simplified to
 * ```
 * 2 * (vci - vcj) * t + 2 * (ci(0) - cj(0)) = 0
 * t = - (ci(0) - cj(0)) / (vci - vcj)
 * ```
 * If the speeds are also the same, then the equation becomes
 * ```
 * ci(0) - cj(0) = 0
 * ```
 * which means that if `ci(0) = cj(0)` then the particles collide already at `t=0`,
 * otherwise there's such `t` and the particles don't collide.
 *
 * Two particles potentially collide, if the equations for coordinates x, y, and z
 * have equal non-negative integer solution.
 *
 * We solve the equations for all particle pairs, sort the results by `t`,
 * and eliminate colliding particles chronologically.
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

    fun countParticlesRemainingAfterCollisions(particles: List<Particle3D<Int>>): Int {
        val collisions = mutableMapOf<Int, MutableSet<Particle3D<Int>>>()
        for (i in particles.indices) {
            val pi = particles[i]
            for (j in i + 1 until particles.size) {
                val pj = particles[j]
                val txSolutions = solveForT(pi.a.x, pj.a.x, pi.v.x, pj.v.x, pi.p.x, pj.p.x)
                if (txSolutions.isEmpty()) continue
                val tySolutions = solveForT(pi.a.y, pj.a.y, pi.v.y, pj.v.y, pi.p.y, pj.p.y)
                if (tySolutions.isEmpty()) continue
                val tzSolutions = solveForT(pi.a.z, pj.a.z, pi.v.z, pj.v.z, pi.p.z, pj.p.z)
                if (tzSolutions.isEmpty()) continue
                val commonSolutions = txSolutions intersect tySolutions intersect tzSolutions
                val t = commonSolutions.minNonNegativeOrNull() ?: continue // skip if no common solutions
                collisions.getOrPut(t) { hashSetOf() }.apply {
                    add(pi)
                    add(pj)
                }
            }
        }

        val eliminatedParticleIds = hashSetOf<Int>()
        for ((_, particles) in collisions.entries.sortedBy { (time, _) -> time }) {
            val activeParticles = particles.filter { it.id !in eliminatedParticleIds }
            if (activeParticles.size > 1) {
                activeParticles.forEach { eliminatedParticleIds += it.id }
            }
        }
        return particles.size - eliminatedParticleIds.size
    }

    private sealed interface IntegerSolutions {
        infix fun intersect(other: IntegerSolutions): IntegerSolutions = when (this) {
            is NoIntegers   -> NoIntegers
            is AllIntegers  -> other
            is SomeIntegers -> when (other) {
                is NoIntegers   -> NoIntegers
                is AllIntegers  -> this
                is SomeIntegers -> SomeIntegers(solutions intersect other.solutions)
            }
        }

        fun isEmpty() = this is NoIntegers || this is SomeIntegers && this.solutions.isEmpty()

        fun minNonNegativeOrNull(): Int? = when (this) {
            is NoIntegers   -> null
            is AllIntegers  -> 0
            is SomeIntegers -> solutions.filter { it >= 0 }.minOrNull()
        }
    }

    private data object NoIntegers : IntegerSolutions
    private data class SomeIntegers(val solutions: Set<Int>) : IntegerSolutions
    private data object AllIntegers : IntegerSolutions

    private fun solveForT(ai: Int, aj: Int, vi: Int, vj: Int, ci: Int, cj: Int): IntegerSolutions {
        if (ai == aj) return solveForTIfEqualA(vi, vj, ci, cj)

        val A = ai - aj
        val B = ai - aj + 2 * (vi - vj)
        val C = 2 * (ci - cj)

        val D = B * B - 4 * A * C
        if (D < 0) return NoIntegers // no solution

        val sqrtD = sqrt(D.toDouble()).roundToInt()
        if (sqrtD * sqrtD != D) return NoIntegers // no integer solution

        return SomeIntegers(buildSet {
            if ((-B - sqrtD) % (2 * A) == 0) {
                add((-B - sqrtD) / (2 * A))
            }
            if ((-B + sqrtD) % (2 * A) == 0) {
                add((-B + sqrtD) / (2 * A))
            }
        })
    }

    private fun solveForTIfEqualA(vi: Int, vj: Int, ci: Int, cj: Int): IntegerSolutions = when {
        vi == vj                   -> solveForTIfEqualV(ci, cj)
        (cj - ci) % (vi - vj) == 0 -> SomeIntegers(setOf((cj - ci) / (vi - vj)))
        else                       -> NoIntegers
    }

    private fun solveForTIfEqualV(ci: Int, cj: Int): IntegerSolutions = if (ci == cj) AllIntegers else NoIntegers
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

    // Part 2
    val res2 = solver.countParticlesRemainingAfterCollisions(particles)
    println("Particle that will remain after all collisions: $res2")
}

fun main() {
    printResult("input.txt")
}
