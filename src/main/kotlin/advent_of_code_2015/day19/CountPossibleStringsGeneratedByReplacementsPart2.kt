package advent_of_code_2015.day19

import advent_of_code_2015.day19.ReplacementsInputReader.readInput

/**
 * [Medicine for Rudolph](https://adventofcode.com/2015/day/19)
 *
 * Given a string `s` and a collection of possible letter replacement steps,
 * find the minimum number of steps to generate `s` from a single electron `"e"`.
 *
 * For example, given the replacements
 * - `e => H`
 * - `e => O`
 * - `H => HO`
 * - `H => OH`
 * - `O => HH`
 *
 * we can generate `HOH` in 3 steps
 * - `e => O` via `e => O`
 * - `O => HH` via `O => HH`
 * - `HH => HOH` via `H => OH` on the second `H`
 *
 * and `HOHOHO` in 6 steps.
 *
 *
 * Solution uses the idea of randomized greedy search
 * seen in some solutions on [reddit thread](https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/).
 *
 */
class CountPossibleStringsGeneratedByReplacementsPart2 {
    fun countStepsToGenerateByReplacements(s: String, replacements: Map<String, List<String>>) =
        RandomizedGreedyStepsCounter(s, replacements).countStepsGreedyRandomized()


    private class RandomizedGreedyStepsCounter(val target: String, replacements: Map<String, List<String>>) {
        /**
         * Reduction steps from a replacement result back to replacement source, e.g.,
         * for replacement `H => HO, H => OH` the `reductions` would contain the inverse replacmenets
         * `HO => H, OH => H`
         */
        private val reductions = replacements.asSequence()
                .map { (src, destList) -> destList.map { it to src } }
                .flatten()
                .sortedByDescending { it.first.length }
                .toList()


        fun countStepsGreedyRandomized(): Int {
            var steps: Int
            do {
                steps = countStepsGreedyRandomized(target)
            } while (steps == UNREACHABLE)
            return steps
        }

        private fun countStepsGreedyRandomized(s: String): Int {
            if (s == REDUCTION_TARGET) return 0

            for ((src, dest) in reductions.shuffled()) {
                val i = s.indexOf(src)
                if (i >= 0) {
                    val steps = countStepsGreedyRandomized(s.replaceRange(i until i + src.length, dest))
                    return if (steps == UNREACHABLE) UNREACHABLE else steps + 1
                }
            }
            return UNREACHABLE
        }

        companion object {
            private const val REDUCTION_TARGET = "e"
            private const val UNREACHABLE = -1
        }
    }
}

private fun printResult(inputFileName: String) {
    val (s, replacements) = readInput(inputFileName)
    val solver = CountPossibleStringsGeneratedByReplacementsPart2()
    val res = solver.countStepsToGenerateByReplacements(s, replacements)
    println("Minimum steps to generate target string: $res")
}

fun main() {
    printResult("input.txt")
}