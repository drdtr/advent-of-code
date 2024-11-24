package advent_of_code_2017.day24

import Util.readInputLines
import advent_of_code_2017.day24.MaxPathWeightWithTwoSidedConnectorsPart1.*

object TwoSidedConnectors {
    data class Connector(val a: Int, val b: Int) {
        val weight: Int
            get() = a + b

        fun otherSideValue(thisSideValue: Int) = when (thisSideValue) {
            a    -> b
            b    -> a
            else -> error("$thisSideValue not a side value in $this")
        }
    }

    fun readInput(inputFileName: String): List<Connector> =
        readInputLines(2017, 24, inputFileName).map { line ->
            line.split("/")
                    .map { it.trim().toInt() }
                    .let { (a, b) -> Connector(a, b) }
        }
}