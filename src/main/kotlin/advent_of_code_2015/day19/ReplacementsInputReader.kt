package advent_of_code_2015.day19

import Util.readInputLines

object ReplacementsInputReader {
    data class Input(val s: String, val replacements: Map<String, List<String>>)

    fun readInput(inputFileName: String): Input {
        val lines = readInputLines(2015, 19, inputFileName)
        val separatingLineIndex = lines.size - 2
        val replacements = lines.subList(0, separatingLineIndex).asSequence()
                .map { line -> line.split(" ").let { it[0] to it[2] } }
                .groupBy { replacement -> replacement.first }
                .mapValues { (_, pairs) -> pairs.map { it.second } }
        val s = lines.last()
        return Input(s, replacements)
    }
}