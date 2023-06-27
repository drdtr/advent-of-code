package advent_of_code_2015.day24

import Util.readInputLines
import java.math.BigInteger

/**
 * [It Hangs in the Balance](https://adventofcode.com/2015/day/24)
 *
 * Given a list of numbers , divide them into `k` groups, such that the sums of the groups are equal
 * and the size of the first group is minimal,
 * and return the minimum possible product of the minimal first group.
 */
class MinimizeProductForEqualSums {
    fun findMinimumProductForEqualSumsAndSmallestGroup(nums: List<Int>, k: Int): Long =
        MinProductFinder(nums, k).findMinProduct()

    private class MinProductFinder(nums: List<Int>, val k: Int) {
        private val n = nums.size
        private val numsDesc = LongArray(n) { nums[it].toLong() }.apply { sortDescending() }
        private val numSum = nums.sum().also {
            require(it % k == 0) { "Sum of all numbers must be divisible by $k" }
        }
        private val targetGroupSum = numSum / k

        private val firstGroup = mutableListOf<Long>()
        private var minSize = n
        private var minProduct = Long.MAX_VALUE
        private val isUsedNum = BooleanArray(n)

        fun findMinProduct(): Long {
            checkFirstGroup()
            return minProduct
        }

        private fun checkFirstGroup(currGroupSum: Long = 0, currGroupProduct: BigInteger = BigInteger.ONE) {
            if (firstGroup.size >= minSize) {
                // we won't be able to construct a smaller 1st group than then one we already had
                return
            }
            for (i in 0 until n) {
                if (isUsedNum[i]) continue
                val ni = numsDesc[i]

                if (minSize < n) {
                    // we already have a valid 1st group with minSize < initial value `n`
                    // then check if we can reach targetGroupSum with a group of size minSize
                    val remainingNumsCount = minSize - firstGroup.size
                    if (currGroupSum + ni * remainingNumsCount < targetGroupSum) {
                        // the remaining numbers in the array, which is sorted in descending order, are too small
                        // to reach targetGroupSum by adding at most `remainingNumsCount` numbers
                        return
                    }
                }

                val newGroupSum = currGroupSum + ni
                val newGroupProduct = currGroupProduct * ni.toBigInteger()
                if (newGroupSum > targetGroupSum) continue
                isUsedNum[i] = true
                firstGroup.add(ni)
                if (newGroupSum < targetGroupSum) {
                    checkFirstGroup(newGroupSum, newGroupProduct)
                } else if (hasRemainingGroups(1)) {
                    recordMinSizeAndProduct(firstGroup.size, newGroupProduct.toLong())
                }
                firstGroup.removeLast()
                isUsedNum[i] = false
            }
        }

        fun recordMinSizeAndProduct(size: Int, product: Long) {
            if (minSize >= size) {
                if (minSize > size) {
                    // we found a smaller group, so we record both its size and product
                    minSize = size
                    minProduct = product
//                    println("New minSize=$minSize, new minProduct=$minProduct, numGroups=$firstGroup")
                } else {
                    // for a group with same size, we record the product only if it's smaller
                    if (minProduct > product) {
//                        println("Existing minSize=$minSize, new minProduct=$minProduct, numGroups=$firstGroup")
                        minProduct = product
                    }
                }
            }
        }

        private fun hasRemainingGroups(groupIndex: Int = 1, currGroupSum: Long = 0): Boolean {
            if (groupIndex == k - 1) {
                // we don't even need to construct and check the last group because the first `k - 1` groups
                // already have sum equal to `numSum / k`, so the last group will also have it.
                return true
            }

            for (i in 0 until n) {
                if (isUsedNum[i]) continue
                val ni = numsDesc[i]

                val remainingNumsCount = n - i
                if (currGroupSum + ni * remainingNumsCount < targetGroupSum) {
                    // the remaining `n - i` numbers in the array, which is sorted in descending order, are too small
                    // to reach targetGroupSum by adding at most `remainingNumsCount` numbers
                    return false
                }

                val newGroupSum = currGroupSum + ni
                if (newGroupSum > targetGroupSum) continue
                isUsedNum[i] = true
                val hasRemainingGroups =
                    if (newGroupSum < targetGroupSum) {
                        hasRemainingGroups(groupIndex, newGroupSum)
                    } else {
                        hasRemainingGroups(groupIndex + 1)
                    }
                isUsedNum[i] = false

                if (hasRemainingGroups) {
                    return true
                }
            }
            return false
        }
    }
}

private fun readInput(inputFileName: String): List<Int> =
    readInputLines(2015, 24, inputFileName).map { it.toInt() }

private fun printResult(inputFileName: String) {
    val nums = readInput(inputFileName)
    val solver = MinimizeProductForEqualSums()
    for (k in listOf(3, 4)) {
        val minProduct = solver.findMinimumProductForEqualSumsAndSmallestGroup(nums, k)
        println("Minimum product of smallest first group of $k groups with equal sum: $minProduct")
    }
}

fun main() {
    printResult("input.txt")
}

