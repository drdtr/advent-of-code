package advent_of_code_2015.day14

import Util.readInputLines

object ReindeerInputReader {
    fun readInput(inputFileName: String): Pair<Int, List<Reindeer>> {
        val lines = readInputLines(2015, 14, inputFileName)
        val racingTime = lines[0].toInt()
        val reindeers = lines.subList(1, lines.size).asSequence()
                .map { it.split(" ", ".") }
                .map { Reindeer(it[0], it[3].toInt(), it[6].toInt(), it[13].toInt()) }
                .toList()
        return racingTime to reindeers
    }
}