package advent_of_code_2017.day09

import Util.readInputLines

/**
 * [Stream Processing](https://adventofcode.com/2017/day/9)
 *
 * Given a stream of characters that can contain "garbage", clean the stream and find a _total score_ of the stream.
 *
 * The char stream can contain _groups_ `{..}` or _garbage_ `<..>`. Also `!` cancels any char after it.
 * Each group can contain any number of groups or garbage, separated by commas.
 *
 * Examples
 * - `<>` - empty garbage
 * - `<any chars>` - garbage
 * - `<<<<>` - garbage containing `<<<` - because only the delimiting `<...>` are relevant, the other chars `<` are ignored.
 * - `<{!>}>` - garbage containing `{!>}` because the first `>` is canceled and thus doesn't close the garbage sequence
 * - `<!!>` - garbage containing `!!`: the 2nd `!` is canceled and `>` terminates the garbage.
 * - `<!!!>>` - garbage containing `!!!>` because the 2nd `!` and the 1st `>` are canceled.
 * - `<{o"i!a,<{i<a>` - garbage with random chars that ends at the 1st `>`.
 *
 * Examples of whole streams and their numbers of groups:
 * - `{}` - 1 group.
 * - `{{{}}}` - 3 groups.
 * - `{{},{}}` - 3 groups.
 * - `{{{},{},{{}}}}` - 6 groups.
 * - `{<{},{},{{}}>}` - 1 group (which itself contains garbage).
 * - `{<a>,<a>,<a>,<a>}` - 1 group.
 * - `{{<a>},{<a>},{<a>},{<a>}}` - 5 groups.
 * - `{{<!>},{<!>},{<!>},{<a>}}`  2 groups because all `>` but the last are canceled.
 *
 * ### Part 1
 *
 * The goal is to find the _total score_ of all groups.
 * Each group's score is the score of all groups that contain it plus 1:
 * - `{}` - score 1.
 * - `{{{}}}` - score 1 + 2 + 3 = 6.
 * - `{{},{}}` - score 1 + 2 + 2 = 5.
 * - `{{{},{},{{}}}}` - score 1 + 2 + 3 + 3 + 3 + 4 = 16.
 * - `{<a>,<a>,<a>,<a>}` - score 1.
 * - `{{<ab>},{<ab>},{<ab>},{<ab>}}` - score 1 + 2 + 2 + 2 + 2 = 9.
 * - `{{<!!>},{<!!>},{<!!>},{<!!>}}` - score 1 + 2 + 2 + 2 + 2 = 9.
 * - `{{<a!>},{<a!>},{<a!>},{<ab>}}` - score 1 + 2 = 3.
 *
 * ### Part 2
 *
 * Now, count the removed garbage characters, ignoring the delimiters `<`, `>`,
 * the cancel char `!` and the chars canceled by `!`:
 * - `<>` - 0 characters.
 * - `<random characters>` - 17 characters.
 * - `<<<<>` - 3 characters.
 * - `<{!>}>` - 2 characters.
 * - `<!!>` - 0 characters.
 * - `<!!!>>` - 0 characters.
 * - `<{o"i!a,<{i<a>` - 10 characters.
 */
class CharStreamScore {
    data class Group(val childGroups: List<Group>) {
        override fun toString() = childGroups.joinToString(prefix = "{", separator = ",", postfix = "}")
    }

    fun calcTotalScore(s: String) = calcTotalScoreAndCountRemovedGarbageChars(s).totalScore

    fun countRemovedGarbageChars(s: String) = calcTotalScoreAndCountRemovedGarbageChars(s).removedGarbageCharsCount

    private data class TotalScoreAndRemovedGarbageCharsCount(val totalScore: Int, val removedGarbageCharsCount: Int)

    private fun calcTotalScoreAndCountRemovedGarbageChars(s: String): TotalScoreAndRemovedGarbageCharsCount {
        val (cleanedStr, removedGarbageCharsCount) = s.removeCanceledChars().removeGarbage()
        val rootGroup = GroupParser(cleanedStr).parse()
        return TotalScoreAndRemovedGarbageCharsCount(
            totalScore = rootGroup.totalScore(),
            removedGarbageCharsCount = removedGarbageCharsCount,
        )
    }

    private fun String.removeCanceledChars(): String {
        val sb = StringBuilder(length)
        var i = 0
        while (i < length) {
            if (this[i] == '!') {
                i += 2
            } else {
                sb.append(this[i++])
            }
        }
        return sb.toString()
    }

    private fun String.removeGarbage(): Pair<String, Int> {
        var cntRemoved = 0
        val sb = StringBuilder(length)
        var isGarbage = false
        for (i in indices) when {
            !isGarbage && this[i] == '<' -> isGarbage = true
            !isGarbage                   -> sb.append(this[i])
            isGarbage && this[i] == '>'  -> isGarbage = false
            isGarbage                    -> cntRemoved++
        }
        return sb.toString() to cntRemoved
    }

    private class GroupParser(val s: String) {
        fun parse(): Group = parseGroupAt(0).first

        private fun parseGroupAt(beginIndex: Int): Pair<Group, Int> {
            require(s[beginIndex] == '{') { "Group must start with '{' but 's[$beginIndex]' was '${s[beginIndex]}' " }
            val childGroups = mutableListOf<Group>()

            var i = beginIndex + 1
            while (i < s.length) when (s[i++]) {
                ',', ' ' -> {} // skip
                '}'      -> break // end of group
                '{'      -> parseGroupAt(i - 1).let { (group, endIndexExcl) ->
                    childGroups.add(group)
                    i = endIndexExcl
                }
            }

            return Group(childGroups) to i
        }
    }

    private fun Group.totalScore(currScore: Int = 1): Int =
        currScore + childGroups.sumOf { it.totalScore(currScore + 1) }
}

private fun readInput(inputFileName: String): String =
    readInputLines(2017, 9, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val s = readInput(inputFileName)
    val solver = CharStreamScore()

    // part 1
    val res1 = solver.calcTotalScore(s)
    println("Total score: $res1")

    // part 2
    val res2 = solver.countRemovedGarbageChars(s)
    println("Removed garbage chars count: $res2")
}

fun main() {
    printResult("input.txt")
}
