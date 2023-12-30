package advent_of_code_2017.day01

import Util.readInputLines

/**
 * [Inverse Captcha](https://adventofcode.com/2017/day/1)
 *
 * Given a string `s` of decimal digits, calculate the sum of all digits that match the digit that's `shift` away.
 * The string is considered as circular, i.e., the last digit is followed by the first digit.
 *
 * ### Part 1
 *
 * Matching next digit, i.e., `shift = 1`
 *
 * Examples:
 * - `1122` - the sum is 3 because the 1st digit `1` is same as 2nd digit, and the 3rd digit `2` is same as 4th digit
 * - `1111` - the sum is 4 each digit `1` is followed by `1`, incl. the last `1` which is followed by the 1st `1`
 * - `1213` - the sum is 0 because there are no subsequent equal digits
 *
 *
 * ### Part 2
 *
 * Matching digit halway around, i.e., `shift = digits.length / 2`
 *
 * Examples:
 * - `1212` - the sum is 6 because the list has length 4, and all four digits match the digit 2 positions ahead
 * - `123425` - the sum is 4 because both 2s match each other
 * - `123123` - the sum is 12
 * - `12131415` - the sum is 4
 *
 */
class SumOfMatchingDigits {
    fun sumOfMatchingDigits(digits: String, shift: Int = 0): Int {
        if (digits.length < 2) return 0

        var sum = 0
        for (i in digits.indices) {
            if (digits[i] == digits[(i + shift) % digits.length]) sum += (digits[i] - '0')
        }
        return sum
    }
}


private fun readInput(inputFileName: String): String =
    readInputLines(2017, 1, inputFileName)[0]

private fun printResult(inputFileName: String) {
    val digitStr = readInput(inputFileName)
    val solver = SumOfMatchingDigits()

    // part 1
    val res1 = solver.sumOfMatchingDigits(digitStr, shift = 1)
    println("Sum of digits matching the next digit: $res1")
    // part 2
    val res2 = solver.sumOfMatchingDigits(digitStr, shift = digitStr.length / 2)
    println("Sum of digits matching the digit halfway around: $res2")
}

fun main() {
    printResult("input.txt")
}
