package advent_of_code_2018.day04

import Util
import advent_of_code_2018.day04.GuardLog.*
import advent_of_code_2018.day04.GuardLog.GuardLogEntry.*
import java.time.LocalDateTime
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * [Repose Record](https://adventofcode.com/2018/day/4)
 *
 * You're given a log of guards:
 * - timestamps are in the format `year-month-day hour:minute`
 * - Each entry is:
 *    - `Guard #n begins shift`: the guard with id `n` begins the shift
 *    - `falls asleep`: the current guard falls asleep
 *    - `wakes up`: the current guard wakes up
 *
 * The log entries can be in arbitrary order.
 *
 * Example (in chronological order):
 * ```
 * [1518-11-01 00:00] Guard #10 begins shift
 * [1518-11-01 00:05] falls asleep
 * [1518-11-01 00:25] wakes up
 * [1518-11-01 00:30] falls asleep
 * [1518-11-01 00:55] wakes up
 * [1518-11-01 23:58] Guard #99 begins shift
 * [1518-11-02 00:40] falls asleep
 * [1518-11-02 00:50] wakes up
 * [1518-11-03 00:05] Guard #10 begins shift
 * [1518-11-03 00:24] falls asleep
 * [1518-11-03 00:29] wakes up
 * [1518-11-04 00:02] Guard #99 begins shift
 * [1518-11-04 00:36] falls asleep
 * [1518-11-04 00:46] wakes up
 * [1518-11-05 00:03] Guard #99 begins shift
 * [1518-11-05 00:45] falls asleep
 * [1518-11-05 00:55] wakes up
 * ```
 *
 * The guards 10 and 99 are asleep in the minutes denoted by `#`:
 * ```
 * Date   ID   Minute
 *             000000000011111111112222222222333333333344444444445555555555
 *             012345678901234567890123456789012345678901234567890123456789
 * 11-01  #10  .....####################.....#########################.....
 * 11-02  #99  ........................................##########..........
 * 11-03  #10  ........................#####...............................
 * 11-04  #99  ....................................##########..............
 * 11-05  #99  .............................................##########.....
 * ```
 *
 *
 * ### Part 1
 *
 * Find the guard who is the most minutes asleep, and find the minutes he's most likely to be asleep.
 * Return the product of the guard id and minute.
 *
 * In the example above, it's the guard 10, and he's most likely to be asleep on minute 24.
 *
 * ### Part 2
 *
 * Find the guard who is most frequently asleep on the same minute.
 * Return the product of the guard id and minute.
 *
 * In the example above, it's the guard 99, and he's most frequently asleep on minute 45.
 */
class GuardLog {
    sealed class GuardLogEntry(open val timestamp: LocalDateTime) {
        data class GuardBeginsShiftEntry(override val timestamp: LocalDateTime, val guardId: Int) :
            GuardLogEntry(timestamp)

        data class GuardFallsAsleepEntry(override val timestamp: LocalDateTime) : GuardLogEntry(timestamp)
        data class GuardWakesUpEntry(override val timestamp: LocalDateTime) : GuardLogEntry(timestamp)
    }

    data class MostAsleepGuard(val guardId: Int, val mostFrequentMinute: Int)

    private class AsleepMinuteCounts {
        data class MinuteAndCount(val minute: Int, val count: Int)

        private val minuteCounts = IntArray(24 * 60)
        fun increment(hour: Int, minute: Int) {
            require(hour in 0..23) { "Hour must be between 0 and 23 but was $hour" }
            require(minute in 0..59) { "Minute must be between 0 and 59 but was $minute" }
            minuteCounts[hour * 24 + minute]++
        }

        fun getCount(hour: Int, minute: Int): Int {
            require(hour in 0..23) { "Hour must be between 0 and 23 but was $hour" }
            require(minute in 0..59) { "Minute must be between 0 and 59 but was $minute" }
            return minuteCounts[hour * 24 + minute]
        }

        fun countMinutesAsleep() = minuteCounts.sum()

        fun mostFrequentMinute(): MinuteAndCount {
            var mostFrequentMinute = 0
            var maxCount = 0
            for (hour in 0..23) for (minute in 0..59) {
                val count = getCount(hour, minute)
                if (maxCount < count) {
                    maxCount = count
                    mostFrequentMinute = minute
                }
            }
            return MinuteAndCount(minute = mostFrequentMinute, count = maxCount)
        }
    }

    fun findMostAsleepGuard(logEntries: List<GuardLogEntry>): MostAsleepGuard {
        val asleepMinuteCountsByGuardId = countAsleepMinutesByGuard(logEntries)
        val mostAsleepGuardId = countAsleepMinutesByGuard(logEntries)
                .maxBy { (_, asleepMinuteCounts) -> asleepMinuteCounts.countMinutesAsleep() }
                .key
        val mostFrequentAsleepMinute = asleepMinuteCountsByGuardId[mostAsleepGuardId]!!.mostFrequentMinute()
        return MostAsleepGuard(guardId = mostAsleepGuardId, mostFrequentMinute = mostFrequentAsleepMinute.minute)
    }

    fun findMostAsleepGuardOnSameMinute(logEntries: List<GuardLogEntry>): MostAsleepGuard {
        val mostFrequentAsleepMinuteByGuardId = countAsleepMinutesByGuard(logEntries)
                .mapValues { (_, asleepMinuteCounts) -> asleepMinuteCounts.mostFrequentMinute() }
        val mostAsleepGuardId = mostFrequentAsleepMinuteByGuardId
                .maxBy { (_, mostFrequentMinute) -> mostFrequentMinute.count }
                .key
        val mostFrequentAsleepMinute = mostFrequentAsleepMinuteByGuardId[mostAsleepGuardId]!!.minute
        return MostAsleepGuard(guardId = mostAsleepGuardId, mostFrequentMinute = mostFrequentAsleepMinute)
    }

    private fun countAsleepMinutesByGuard(logEntries: List<GuardLogEntry>): Map<Int, AsleepMinuteCounts> {
        val logEntriesOrderedByTimestampAsc = logEntries.sortedBy { it.timestamp }
        val asleepMinuteCountsByGuardId = hashMapOf<Int, AsleepMinuteCounts>()

        fun fillAsleepMinutes(guardId: Int, start: LocalDateTime, endExcl: LocalDateTime) {
            val asleepMinuteCounts = asleepMinuteCountsByGuardId.getOrPut(guardId) { AsleepMinuteCounts() }
            var time = start
            while (time.isBefore(endExcl)) {
                asleepMinuteCounts.increment(time.hour, time.minute)
                time = time.plusMinutes(1)
            }
        }

        val startLogEntry = logEntriesOrderedByTimestampAsc.first()
        require(startLogEntry is GuardBeginsShiftEntry) {
            "First log entry must be a shift begin, but was $startLogEntry"
        }
        var timestamp = startLogEntry.timestamp
        var guardId = startLogEntry.guardId
        var isAsleep = false
        for (logEntry in with(logEntriesOrderedByTimestampAsc) { subList(1, size) }) {
            if (isAsleep) {
                // If the previous guard was asleep, fill his asleep minutes
                fillAsleepMinutes(guardId, timestamp, logEntry.timestamp)
            }
            when (logEntry) {
                is GuardBeginsShiftEntry -> {
                    guardId = logEntry.guardId
                    isAsleep = false
                }

                is GuardFallsAsleepEntry -> isAsleep = true
                is GuardWakesUpEntry     -> isAsleep = false
            }
            timestamp = logEntry.timestamp
        }

        return asleepMinuteCountsByGuardId
    }
}

private object GuardLogInputPatterns {
    private val timestampStr = "\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})]"
    val logEntryBeginsShift = Pattern.compile("$timestampStr Guard #(\\d+) begins shift")
    val logEntryFallsAsleep = Pattern.compile("$timestampStr falls asleep")
    val logEntryWakesUp = Pattern.compile("$timestampStr wakes up")
}

private fun parseLogEntry(s: String): GuardLogEntry {
    fun Matcher.matchedLocalDateTime() = LocalDateTime.of(
        /* year = */ group(1).toInt(),
        /* month = */ group(2).toInt(),
        /* dayOfMonth = */ group(3).toInt(),
        /* hour = */ group(4).toInt(),
        /* minute = */ group(5).toInt(),
    )
    with(GuardLogInputPatterns.logEntryBeginsShift.matcher(s)) {
        if (matches()) return GuardBeginsShiftEntry(matchedLocalDateTime(), group(6).toInt())
    }
    with(GuardLogInputPatterns.logEntryFallsAsleep.matcher(s)) {
        if (matches()) return GuardFallsAsleepEntry(matchedLocalDateTime())
    }
    with(GuardLogInputPatterns.logEntryWakesUp.matcher(s)) {
        if (matches()) return GuardWakesUpEntry(matchedLocalDateTime())
    }
    error("Invalid guards log entry $s")
}

private fun readInput(inputFileName: String): List<GuardLogEntry> =
    Util.readInputLines(2018, 4, inputFileName).map { parseLogEntry(it) }

private fun printResult(inputFileName: String) {
    val logEntries = readInput(inputFileName)
//    println("Number of log entries: ${logEntries.size}")
//    println("Number of unique time stamps: ${logEntries.map { it.timestamp }.toSet().size}")
//    println("Guards log entries: \n${logEntries.sortedBy { it.timestamp }.joinToString(separator = "\n")}")

    val solver = GuardLog()

    // Part 1
    val mostAsleepGuard = solver.findMostAsleepGuard(logEntries)
    println("Most asleep guard: $mostAsleepGuard")
    println("guardId * mostFrequentMinute = ${with(mostAsleepGuard) { guardId * mostFrequentMinute }}")

    // Part 2
    val mostAsleepGuardOnSameMinute = solver.findMostAsleepGuardOnSameMinute(logEntries)
    println("Most asleep guard on same minute: $mostAsleepGuard")
    println("guardId * mostFrequentMinute = ${with(mostAsleepGuardOnSameMinute) { guardId * mostFrequentMinute }}")
}

fun main() {
    printResult("input.txt")
}




