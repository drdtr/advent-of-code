package advent_of_code_2016.day20

import Util.readInputLines

/**
 * [Firewall Rules](https://adventofcode.com/2016/day/20)
 *
 * Given a list of intervals with non-negative integer boundaries,
 * find the smallest integer number not contained in any of the intervals.
 */
class IntegerIntervals {
    data class IntegerIntervalsResult(val minNotInAnyInterval: Long, val totalNumCountInAllIntervals: Long)

    fun findMinNotInIntervalAndTotalNumCount(intervals: Collection<LongRange>): IntegerIntervalsResult {
        if (intervals.isEmpty()) {
            return IntegerIntervalsResult(0, 0)
        }

        val sortedMergedIntervals = mergeIntervals(intervals)
        return IntegerIntervalsResult(
            minNotInAnyInterval = minNumNotInAnyInterval(sortedMergedIntervals),
            totalNumCountInAllIntervals = sortedMergedIntervals.sumOf { it.contiguousIntervalSize() }
        )
    }

    private fun LongRange.contiguousIntervalSize() = last - first + 1

    private fun minNumNotInAnyInterval(sortedDisjointIntervals: List<LongRange>): Long {
        if (sortedDisjointIntervals[0].first > 0) {
            return 0
        }

        for (i in 0..sortedDisjointIntervals.size - 2) {
            val firstAfterInterval = sortedDisjointIntervals[i].last + 1
            if (firstAfterInterval < sortedDisjointIntervals[i + 1].first) {
                return firstAfterInterval
            }
        }

        return sortedDisjointIntervals.last().last + 1
    }

    /**
     * Returns a sorted list of merged intervals, i.e., they contain exactly the same numbers as [intervals],
     * are disjoint, and are in ascending order.
     */
    private fun mergeIntervals(intervals: Collection<LongRange>): List<LongRange> = buildList {
        val sortedIntervals = intervals.sortedBy { it.first }
        var left = sortedIntervals[0].first
        var right = sortedIntervals[0].last
        for (i in 1 until sortedIntervals.size) {
            val interval = sortedIntervals[i]
            if (right >= interval.first) {
                // the intervals overlap, so include in the current merged interval
                if (right < interval.last) {
                    right = interval.last
                }
            } else {
                // the intervals don't overlap, so store the current merged interval and start a new merged interval
                add(left..right)
                left = interval.first
                right = interval.last
            }
        }
        // store the last merged interval
        add(left..right)
    }
}

private fun readInput(inputFileName: String): List<LongRange> =
    readInputLines(2016, 20, inputFileName).map { line ->
        line.split("-").map { it.toLong() }.let { (a, b) -> a..b }
    }

private fun printResult(inputFileName: String) {
    val intervals = readInput(inputFileName)
    val solver = IntegerIntervals()
    val res = solver.findMinNotInIntervalAndTotalNumCount(intervals)
    println("The first integer not in any of the given intervals is ${res.minNotInAnyInterval}")
    println("The total number of integers in all intervals is ${res.totalNumCountInAllIntervals}")
    // total available numbers in the range of unsigned 32-bit integers
    val minPossibleNum = 0
    val maxPossibleNum = (1L shl 32) - 1
    val notCoveredCount = (maxPossibleNum - minPossibleNum + 1) - res.totalNumCountInAllIntervals
    println(
        "Number of integers in the range $minPossibleNum..$maxPossibleNum " +
                "not covered by all intervals: $notCoveredCount"
    )
}

fun main() {
    printResult("input.txt")
}
