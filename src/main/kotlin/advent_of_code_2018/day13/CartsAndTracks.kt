package advent_of_code_2018.day13

import Util.readInputLines
import advent_of_code_2018.day13.CartsAndTracks.CartDirection.*
import advent_of_code_2018.day13.CartsAndTracks.TrackCellType.*
import java.util.*

/**
 * [Mine Cart Madness](https://adventofcode.com/2018/day/13)
 *
 * You're given an `m*n` grid of rails where each grid cell can be one of the following:
 * - ` `: empty
 * - `|` and `-`: straight path
 * - `/` and `\`: curve
 * - `+`: intersection
 *
 * There are several carts on the tracks, their direction can be:
 * - `^`: up
 * - `v`: down
 * - `<`: left
 * - `>`: right
 *
 * Each time a cart arrives at any intersection, it chooses the direction cyclically:
 * - 1st time: left
 * - 2nd time: straight
 * - 3rd time: right
 *
 * A cart has no per-intersection memory, it just counts any intersections.
 *
 * Carts move in `ticks`, a step per time, starting from top left, to bottom right.
 *
 *
 * Examples:
 * - Closed loop
 * ```
 * /----\
 * |    |
 * |    |
 * \----/
 * ```
 * - Two loops with intersections
 * ```
 * /-----\
 * |     |
 * |  /--+--\
 * |  |  |  |
 * \--+--/  |
 *    |     |
 *    \-----/
 * ```
 *
 *
 * ### Part 1
 *
 * Find the coordinates of the first collision.
 *
 * - An example with two carts:
 * ```
 * /->-\
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  | v  |
 * \-+-/  \-+--/
 *   \------/
 *
 * /-->\
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \->--/
 *   \------/
 *
 * /---v
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \-+>-/
 *   \------/
 *
 * /---\
 * |   v  /----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \-+->/
 *   \------/
 *
 * /---\
 * |   |  /----\
 * | /->--+-\  |
 * | | |  | |  |
 * \-+-/  \-+--^
 *   \------/
 *
 * /---\
 * |   |  /----\
 * | /-+>-+-\  |
 * | | |  | |  ^
 * \-+-/  \-+--/
 *   \------/
 *
 * /---\
 * |   |  /----\
 * | /-+->+-\  ^
 * | | |  | |  |
 * \-+-/  \-+--/
 *   \------/
 *
 * /---\
 * |   |  /----<
 * | /-+-->-\  |
 * | | |  | |  |
 * \-+-/  \-+--/
 *   \------/
 *
 * /---\
 * |   |  /---<\
 * | /-+--+>\  |
 * | | |  | |  |
 * \-+-/  \-+--/
 *   \------/
 *
 * /---\
 * |   |  /--<-\
 * | /-+--+-v  |
 * | | |  | |  |
 * \-+-/  \-+--/
 *   \------/
 *
 * /---\
 * |   |  /-<--\
 * | /-+--+-\  |
 * | | |  | v  |
 * \-+-/  \-+--/
 *   \------/
 *
 * /---\
 * |   |  /<---\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \-<--/
 *   \------/
 *
 * /---\
 * |   |  v----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \<+--/
 *   \------/
 *
 * /---\
 * |   |  /----\
 * | /-+--v-\  |
 * | | |  | |  |
 * \-+-/  ^-+--/
 *   \------/
 *
 * /---\
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  X |  |
 * \-+-/  \-+--/
 *   \------/
 * ```
 * The carts eventually collide at position 7,3:
 * ```
 *             111
 *   0123456789012
 * 0 /---\
 * 1 |   |  /----\
 * 2 | /-+--+-\  |
 * 3 | | |  X |  |
 * 4 \-+-/  \-+--/
 * 5   \------/
 * ```
 *
 *
 */
class CartsAndTracks {
    data class Point(val x: Int, val y: Int)

    fun findLocationOfFirstCollision(trackGridWithCarts: List<String>): Point {
        val trackGridAndInitialCarts = splitIntoTrackGridAndCarts(trackGridWithCarts)
        with(trackGridAndInitialCarts) {
            println(trackGrid.joinToString("\n") { it.joinToString("") { it.gridChar.toString() } })
            println(carts.joinToString("\n"))
        }
        val emulator = CartEmulator(trackGridAndInitialCarts)
        while (emulator.collisionPoint == null) {
            emulator.emulateTick()
            println("${emulator.tickCounter} tick(s):")
            println(emulator.getCartPointsAndDirections())
            println()
        }
        println("Collision at ${emulator.collisionPoint}")
        return emulator.collisionPoint!!
    }

    private data class TrackGridAndCarts(
        val trackGrid: List<List<TrackCellType>>,
        val carts: List<Pair<Point, CartDirection>>,
    )

    private fun splitIntoTrackGridAndCarts(trackGridWithCarts: List<String>): TrackGridAndCarts {
        val carts = mutableListOf<Pair<Point, CartDirection>>()
        val trackGrid = trackGridWithCarts.mapIndexed { y, line ->
            buildList {
                line.forEachIndexed { x, ch ->
                    val trackCellType = TrackCellType.ofOrNull(ch)
                    if (trackCellType != null) {
                        add(trackCellType)
                    } else {
                        val cartDirection = CartDirection.of(ch)
                        carts += Point(x, y) to cartDirection
                        add(
                            when (cartDirection) {
                                CartUp, CartDown    -> Vertical
                                CartLeft, CartRight -> Horizontal
                            }
                        )
                    }
                }
            }
        }

        return TrackGridAndCarts(trackGrid, carts)
    }

    private class CartEmulator(trackGridAndCarts: TrackGridAndCarts) {
        private data class MutableCartDirection(
            var direction: CartDirection,
            var crossingCounter: Int = 0,
        )

        private val trackGrid: List<List<TrackCellType>> = trackGridAndCarts.trackGrid
        private val cartPositionsAndDirections: SortedMap<Point, MutableCartDirection> =
            trackGridAndCarts.carts.associateTo(TreeMap(POINT_COMPARATOR)) { (p, d) -> p to MutableCartDirection(d) }

        var collisionPoint: Point? = null
            private set

        var tickCounter: Int = 0
            private set

        fun getCartPointsAndDirections(): List<Pair<Point, CartDirection>> =
            cartPositionsAndDirections.map { (point, mutableCartDirection) -> point to mutableCartDirection.direction }


        fun emulateTick(terminateOnCollision: Boolean = true) {
            tickCounter++
            val currCartPositions = cartPositionsAndDirections.keys.toList()
            for (p in currCartPositions) {
                val cartDir = cartPositionsAndDirections.remove(p) ?: error("Missing map entry: should never happen")
                val trackCell = trackGrid[p.y][p.x]
                require(trackCell != Empty) { "Track cell must not be empty." }
                val lazyErrMsg = { "Cart direction was ${cartDir.direction} but the track cell was $trackCell." }
                val dirNew = when (trackCell) {
                    Vertical   -> {
                        require(cartDir.direction == CartUp || cartDir.direction == CartDown, lazyErrMsg)
                        cartDir.direction // same direction
                    }

                    Horizontal -> {
                        require(cartDir.direction == CartLeft || cartDir.direction == CartRight, lazyErrMsg)
                        cartDir.direction // same direction
                    }

                    RightUp    -> {
                        when (cartDir.direction) {
                            CartUp    -> CartRight
                            CartDown  -> CartLeft
                            CartLeft  -> CartDown
                            CartRight -> CartUp
                        }
                    }

                    RightDown  -> {
                        when (cartDir.direction) {
                            CartUp    -> CartLeft
                            CartDown  -> CartRight
                            CartLeft  -> CartUp
                            CartRight -> CartDown
                        }
                    }

                    Crossing   -> {
                        val currDirOrderNumber =
                            CART_DIR_TO_CLOCKWISE_TURN_ORDER[cartDir.direction]
                                ?: error("No ordering number mapped for cart direction ${cartDir.direction}")
                        // 1st encounter: -1; 2nd encounter: 0; 3rd encounter: +1; 4th encounter: -1, ...
                        val diffClockwise = (cartDir.crossingCounter % 3) - 1
                        val n = CART_DIR_BY_CLOCKWISE_TURN_ORDER.size
                        val newDirOrderNumber = (currDirOrderNumber + diffClockwise + n) % n
                        CART_DIR_BY_CLOCKWISE_TURN_ORDER[newDirOrderNumber]
                            ?: error("No cart direction mapped for ordering number ${cartDir.direction}")
                    }

                    else       -> error("Cart direction was ${cartDir.direction} but the track cell was $trackCell.")
                }

                val (dx, dy) = CART_DIR_TO_COORD_DIFF[dirNew]
                    ?: error("No coordinate diff mapped for cart direction was ${cartDir.direction}.")
                val pNext = Point(p.x + dx, p.y + dy)
                cartDir.direction = dirNew
                if (trackCell == Crossing) {
                    cartDir.crossingCounter++
                }

                if (pNext in cartPositionsAndDirections) {
                    collisionPoint = pNext
                    if (terminateOnCollision) {
                        return
                    }
                    // Otherwise remove the other cart from collision location
                    cartPositionsAndDirections.remove(pNext)
                } else {
                    // No collision, add the current cart at its location
                    cartPositionsAndDirections[pNext] = cartDir
                }
            }
        }

        private companion object {
            val POINT_COMPARATOR = compareBy<Point> { it.y }.thenBy { it.x }

            val CART_DIR_TO_COORD_DIFF = mapOf(
                CartUp to (0 to -1),
                CartDown to (0 to 1),
                CartLeft to (-1 to 0),
                CartRight to (1 to 0),
            )

            val CART_DIR_TO_CLOCKWISE_TURN_ORDER = mapOf(CartUp to 0, CartRight to 1, CartDown to 2, CartLeft to 3)
            val CART_DIR_BY_CLOCKWISE_TURN_ORDER = CART_DIR_TO_CLOCKWISE_TURN_ORDER.map { (k, v) -> v to k }.toMap()

        }
    }

    private enum class TrackCellType(val gridChar: Char) {
        Empty(' '),
        Vertical('|'),
        Horizontal('-'),
        RightUp('/'),
        RightDown('\\'),
        Crossing('+');

        companion object {
            val gridCharToValue = entries.associateBy { it.gridChar }

            fun ofOrNull(gridChar: Char): TrackCellType? = gridCharToValue[gridChar]
        }
    }

    private enum class CartDirection(val directionChar: Char) {
        // Cart characters.
        CartUp('^'),
        CartDown('v'),
        CartLeft('<'),
        CartRight('>');

        companion object {
            val gridCharToValue = entries.associateBy { it.directionChar }

            fun of(gridChar: Char): CartDirection =
                gridCharToValue[gridChar] ?: error("Unknown gridChar: $gridChar")
        }
    }
}


private fun readInput(inputFileName: String): List<String> = readInputLines(2018, 13, inputFileName)

private fun printResult(inputFileName: String) {
    val input = readInput(inputFileName)
//    println(input.joinToString("\n"))

    val solver = CartsAndTracks()

    // part 1
    val res1 = solver.findLocationOfFirstCollision(input)
    println("Location of first collision: $res1")
}

fun main() {
    printResult("input.txt")
}


