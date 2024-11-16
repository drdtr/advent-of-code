package advent_of_code_2017.day22

object MarkedNodeUtil {
    const val EMPTY_NODE = '.'
    const val MARKED_NODE = '#'

    /**
     * Note: flips the strings across the y coordinate because the first string represents
     * the top row of the grid with the max coordinate.
     */
    fun nodeMarkingStringsToBooleanArrays(nodeMarkingStrings: List<String>): List<List<Boolean>> =
        nodeMarkingStrings.map { nodeMarkingStringToBooleanArray(it) }.reversed()

    private fun nodeMarkingStringToBooleanArray(s: String) = s.map {
        when (it) {
            EMPTY_NODE  -> false
            MARKED_NODE -> true
            else        -> error("Invalid marking character $it")
        }
    }
}