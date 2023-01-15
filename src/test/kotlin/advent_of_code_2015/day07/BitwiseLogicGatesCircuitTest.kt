package advent_of_code_2015.day07

import advent_of_code_2015.day07.LogicGateOp.GateExpr.Companion.toWire
import advent_of_code_2015.day07.LogicGateOp.GateExpr.*
import advent_of_code_2015.day07.LogicGateOp.GateExpr.Atom.*
import advent_of_code_2015.day07.LogicGateOp.GateExpr.Atom.Number
import org.junit.Assert.*
import org.junit.Assume.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.*
import kotlin.test.Test

@RunWith(Parameterized::class)
class BitwiseLogicGatesCircuitTest(val expected: Int, val outputWire: String, val gateOps: List<LogicGateOp>) {

    @Test
    fun `test BitwiseLogicGatesGraph`() = with(BitwiseLogicGatesCircuit()) {
        assertEquals(expected, calcLogicGateOutput(gateOps, outputWire))
    }

    companion object {
        private operator fun Int.not() = Not(Number(this))
        private operator fun String.not() = Not(Wire(this))
        private infix fun String.And(wire2: String) = And(Wire(this), Wire(wire2))
        private infix fun String.Or(wire2: String) = Or(Wire(this), Wire(wire2))
        private infix fun String.LShift(shift: Int) = LShift(Wire(this), Number(shift))
        private infix fun String.RShift(shift: Int) = RShift(Wire(this), Number(shift))
        private infix fun Atom.And(a2: Atom) = And(this, a2)
        private infix fun Atom.Or(a2: Atom) = Or(this, a2)
        private infix fun Atom.LShift(shift: Atom) = LShift(this, shift)
        private infix fun Atom.RShift(shift: Atom) = RShift(this, shift)

        private val circuit1 = listOf(
            123 toWire "x",
            456 toWire "y",
            "x" And "y" toWire "d",
            Number(123) And Wire("y") toWire "di",
            Wire("x") And Number(456) toWire "dj",
            Number(123) And Number(456) toWire "dk",
            "x" Or "y" toWire "e",
            "x" LShift 2 toWire "f",
            "y" RShift 2 toWire "g",
            !"x" toWire "h",
            !"y" toWire "i",
            !123 toWire "hi",
            !456 toWire "ii",
            "g" toWire "a",
        )

        @Parameters(name = "{index}: expected={0}, outputWire={1}, gateOps={2}")
        @JvmStatic
        fun testParams() = listOf(
            arrayOf(72, "d", circuit1),
            arrayOf(72, "di", circuit1),
            arrayOf(72, "dj", circuit1),
            arrayOf(72, "dk", circuit1),
            arrayOf(507, "e", circuit1),
            arrayOf(492, "f", circuit1),
            arrayOf(114, "g", circuit1),
            arrayOf(65412, "h", circuit1),
            arrayOf(65079, "i", circuit1),
            arrayOf(65412, "hi", circuit1),
            arrayOf(65079, "ii", circuit1),
            arrayOf(123, "x", circuit1),
            arrayOf(456, "y", circuit1),
            arrayOf(114, "a", circuit1),
        )
    }
}

