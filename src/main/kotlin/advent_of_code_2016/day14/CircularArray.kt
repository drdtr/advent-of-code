package advent_of_code_2016.day14

class CircularArrayList<T>(val maxSize: Int) {
    private val elements = ArrayList<T>(maxSize)
    private var circularStartIndex = 0

    operator fun get(index: Int) = elements[(index + circularStartIndex) % maxSize]

    operator fun plusAssign(element: T) {
        add(element)
    }

    fun add(element: T) {
        if (elements.size < maxSize) {
            elements += element
        } else {
            elements[circularStartIndex] = element
            circularStartIndex = (circularStartIndex + 1) % maxSize
        }
    }

    val size: Int
        get() = elements.size

    override fun toString(): String =
        (0 until size).joinToString(prefix = "[", separator = ", ", postfix = "]") { "${get(it)}" }
}