package advent_of_code_2016.day14

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CircularArrayListTest {

    @Test
    fun `test until maxSize`() {
        val circularList = CircularArrayList<Int>(3)

        circularList += 0
        assertEqualsCircularList(listOf(0), circularList)

        circularList += 1
        assertEqualsCircularList(listOf(0, 1), circularList)

        circularList += 2
        assertEqualsCircularList(listOf(0, 1, 2), circularList)
    }

    @Test
    fun `test above maxSize`() {
        val circularList = CircularArrayList<Int>(3)

        circularList += 0
        circularList += 1
        circularList += 2

        circularList += 3
        assertEqualsCircularList(listOf(1, 2, 3), circularList)

        circularList += 4
        assertEqualsCircularList(listOf(2, 3, 4), circularList)

        circularList += 5
        assertEqualsCircularList(listOf(3, 4, 5), circularList)

        circularList += 6
        assertEqualsCircularList(listOf(4, 5, 6), circularList)

        circularList += 7
        assertEqualsCircularList(listOf(5, 6, 7), circularList)
    }

    private fun <T> assertEqualsCircularList(expected: List<T>, circularList: CircularArrayList<T>) {
        assertEquals(expected.size, circularList.size)
        var index = 0
        for (e in expected) {
            assertEquals(e, circularList[index++])
        }
    }
}