package advent_of_code_2018.day09

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class CircularNonEmptyLinkedListTest {
    @Test
    fun `test initial list`() {
        CircularNonEmptyLinkedList(0).apply {
            assertEquals(listOf(0), toList())
        }
    }

    @Test
    fun `test add 1`() {
        CircularNonEmptyLinkedList(0).apply {
            add(1)
            assertEquals(listOf(1, 0), toList())
        }
    }

    @Test
    fun `test add 2`() {
        CircularNonEmptyLinkedList(0).apply {
            add(1)
            add(2)
            assertEquals(listOf(2, 0, 1), toList())
        }
    }

    @Test
    fun `test get`() {
        CircularNonEmptyLinkedList(0).apply {
            add(1)
            add(2)
            add(3)
            assertEquals(3, get())
        }
    }

    @Test
    fun `test next`() {
        CircularNonEmptyLinkedList(0).apply {
            add(1)
            add(2)
            add(3)
            assertEquals(0, next())
            assertEquals(1, next())
            assertEquals(2, next())
            assertEquals(3, next())
        }
    }

    @Test
    fun `test prev`() {
        CircularNonEmptyLinkedList(0).apply {
            add(1)
            add(2)
            add(3)
            assertEquals(2, prev())
            assertEquals(1, prev())
            assertEquals(0, prev())
            assertEquals(3, prev())
        }
    }

    @Test
    fun `test remove last`() {
        CircularNonEmptyLinkedList(0).apply {
            assertFailsWith<IllegalArgumentException> { remove() }
        }
    }

    @Test
    fun `test remove`() {
        CircularNonEmptyLinkedList(0).apply {
            add(1)
            add(2)
            add(3)
            next()
            next()
            assertEquals(listOf(1, 2, 3, 0), toList())
            remove()
            assertEquals(listOf(2, 3, 0), toList())
        }
    }
}