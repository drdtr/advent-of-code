package advent_of_code_2017.day13

import Util.readInputLines
import advent_of_code_2017.day13.PacketScanners.*
import advent_of_code_2017.day13.PacketScanners.Firewall.Companion.toFirewall
import java.util.regex.Pattern

/**
 * [Packet Scanners](https://adventofcode.com/2017/day/13)
 *
 * You're travelling on a packet through a multi-layers firewall, where some layers have moving scanners.
 * You're given a list of layers that have scanners and the range of the scanner.
 * - at the beginning, you're at layer `0` and each scanner is at position `0` in its respective layer
 * - you move from layer `i` to layer `i + 1` in each step
 * - the scanners move back and forth as follows
 *    - a scanner moves one cell down its range per step until it reaches the end of its range
 *    and then starts moving up until it reaches the top and then starts moving down again.
 * - you get detected by a scanner if you are in its cell the moment you arrive there
 * (esp., you don't get detected if the scanner arrives at a cell the moment you arrive there
 * because the scanner doesn't have the time to detect you before you leave).
 *
 * Example:
 * - Input: scanner layers and ranges
 * ```
 *    0: 3
 *    1: 2
 *    4: 4
 *    6: 4
 * ```
 * - Firewall
 * ```
 *   Step 0:
 *    0   1   2   3   4   5   6
 *   [S] [S] ... ... [S] ... [S]
 *   [ ] [ ]         [ ]     [ ]
 *   [ ]             [ ]     [ ]
 *                   [ ]     [ ]
 *
 *   Step 1:
 *    0   1   2   3   4   5   6
 *   [ ] [ ] ... ... [ ] ... [ ]
 *   [S] [S]         [S]     [S]
 *   [ ]             [ ]     [ ]
 *                   [ ]     [ ]
 *
 *   Step 2:
 *    0   1   2   3   4   5   6
 *   [ ] [S] ... ... [ ] ... [ ]
 *   [ ] [ ]         [ ]     [ ]
 *   [S]             [S]     [S]
 *                   [ ]     [ ]
 *
 *   Step 3:
 *    0   1   2   3   4   5   6
 *   [ ] [ ] ... ... [ ] ... [ ]
 *   [S] [S]         [ ]     [ ]
 *   [ ]             [ ]     [ ]
 *                   [S]     [S]
 * ```
 *
 * In this example
 * - you get detected in layers 0 and 6 because the packet enters them when the scanner is there
 * - you don't get detected in layer 1 because the scanner moved into the top cell when you were already there.
 *
 *
 * ### Part 1
 * The `severity` of getting caught is equal to *depth* (the number of the layer) times the *range* of the layer.
 * In the example above, the trip severity is `0 * 3 + 6 * 4 = 24`.
 * *
 * Calculate the severity of the whole trip.
 *
 * ### Part 2
 *
 * You can delay the start of the trip by any number of steps.
 * What is the minimum delay with which you don't get caught?
 *
 */
class PacketScanners {
    @JvmInline
    value class Firewall(val scannerRanges: Map<Int, Int>) {
        companion object {
            fun Map<Int, Int>.toFirewall() = Firewall(this)
        }
    }

    fun calcTripSeverity(firewall: Firewall): Int {
        return firewall.scannerRanges
                .map { (layer, range) -> if (isScannerAtPositionZero(layer, range)) layer * range else 0 }
                .sum()
    }

    fun minDelayWithoutBeingCaught(firewall: Firewall): Int {
        fun isCaught(delay: Int) =
            firewall.scannerRanges.any { (layer, range) -> isScannerAtPositionZero(delay + layer, range) }

        val maxDelay = Int.MAX_VALUE
        for (delay in 0 until Int.MAX_VALUE) {
            if (!isCaught(delay)) return delay
        }
        error("Couldn't find a solution until $maxDelay.")
    }

    private fun isScannerAtPositionZero(stepNumber: Int, scannerRange: Int): Boolean {
        val cycleLen = (scannerRange - 1) * 2
        return stepNumber % cycleLen == 0
    }
}

private val scannerLayerInputMatcher = Pattern.compile("(\\d+): (\\d+)")

private fun readInput(inputFileName: String): Firewall =
    readInputLines(2017, 13, inputFileName).map { line ->
        with(scannerLayerInputMatcher.matcher(line)) {
            require(matches()) { "Invalid input line: $line" }
            val layer = group(1).toInt()
            val range = group(2).toInt()
            layer to range
        }
    }.toMap().toFirewall()

private fun printResult(inputFileName: String) {
    val firewall = readInput(inputFileName)
    val solver = PacketScanners()

    // part 1
    val res1 = solver.calcTripSeverity(firewall)
    println("Total trip severity: $res1")

    // part 2
    val res2 = solver.minDelayWithoutBeingCaught(firewall)
    println("Minimum delay without being caught: $res2")
}

fun main() {
    printResult("input.txt")
}
