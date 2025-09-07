package advent_of_code_2018.day09

/**
 * Represents a circular list.
 *
 * This is a simplified version that is required to have at least one element.
 */
class CircularNonEmptyLinkedList<T>(firstElement: T) {
    class Node<T>(val value: T, var next: Node<T>? = null, var prev: Node<T>? = null)

    private var currNode = Node(firstElement).also {
        it.next = it
        it.prev = it
    }

    /** Returns the value of the current node. */
    fun get(): T = currNode.value

    /** Moves one node clockwise. */
    fun next(): T {
        currNode = currNode.next!!
        return currNode.value
    }

    /** Moves one node counter-clockwise. */
    fun prev(): T {
        currNode = currNode.prev!!
        return currNode.value
    }

    /** Adds a node after the current node and makes the added node the current one. */
    fun add(e: T) {
        val node = Node(e)
        node.prev = currNode
        node.next = currNode.next
        currNode.next = node
        node.next!!.prev = node
        currNode = node
    }

    /** Removes the current node and makes the node after it the current one. */
    fun remove() {
        require(currNode.next != currNode) { "Cannot remove last element of a circular list." }
        val next = currNode.next
        currNode.prev!!.next = next
        currNode = next!!
    }

    /** Returns a linear list, containing all nodes of this circular list, in clockwise order,
     * starting with the current node. */
    fun toList(): List<T> {
        val res = mutableListOf<T>()
        var node = currNode
        do {
            res.add(node.value)
            node = node.next!!
        } while (node !== currNode)
        return res
    }
}