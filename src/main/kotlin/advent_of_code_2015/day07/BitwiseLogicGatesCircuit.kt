package advent_of_code_2015.day07

import Util.readInputLines
import advent_of_code_2015.day07.LogicGateOp.*
import advent_of_code_2015.day07.LogicGateOp.GateExpr.*
import advent_of_code_2015.day07.LogicGateOp.GateExpr.Atom.*
import advent_of_code_2015.day07.LogicGateOp.GateExpr.Atom.Number
import advent_of_code_2015.day07.LogicGateOp.GateExpr.Companion.toWire
import java.util.regex.Pattern

/**
 * [Some Assembly Required](https://adventofcode.com/2015/day/7)
 *
 * You're given a circuit of logic gates, specified as a list wire connections between gates:
 * - A bitwise logic gate processes 16-bit integers and can be a bitwise `AND`, `OR`, `LSHIFT`, `RSHIFT`, or `NOT`
 * - An wire connection has the form `<expr> -> <output_wire_id>`:
 *    - `<output_wire_id>` is a lowercase identifier of a wire
 *    - `<expr>` can be
 *       - another wire
 *       - an unsigned 16-bit integer
 *       - a logic gate calculating the output from the inputs from other expressions
 *
 *
 * For a given list of wire connections, calculate the signal provided to `wire a`.
 *
 *
 * ### Examples
 * Simple circuit:
 * - `123 -> x`
 * - `456 -> y`
 * - `x AND y -> d`
 * - `x OR y -> e`
 * - `x LSHIFT 2 -> f`
 * - `y RSHIFT 2 -> g`
 * - `NOT x -> h`
 * - `NOT y -> i`
 *
 * Resulting values on the wires after an emulation run:
 * - `d: 72`
 * - `e: 507`
 * - `f: 492`
 * - `g: 114`
 * - `h: 65412`
 * - `i: 65079`
 * - `x: 123`
 * - `y: 456`
 */
class BitwiseLogicGatesCircuit {
    fun calcLogicGateOutput(gateOps: Iterable<LogicGateOp>, outputWire: String) =
        GateEvaluator(gateOps).calcOutput(outputWire)

    private class GateEvaluator(gateOps: Iterable<LogicGateOp>) {
        /**
         * Map associating output wires to their corresponding gates, i.e.,
         * the result of wire `x` would be calculated by the gate `opByOutputWire[x]`
         */
        private val opByOutputWire = gateOps.associateBy { it.outputWire }

        /** Stores calculated outputs of wires */
        private val wireOutputs = hashMapOf<String, Int>()

        fun calcOutput(outputWire: String): Int = calcOutput(Wire(outputWire))

        private fun calcOutput(outputWire: Wire): Int {
            wireOutputs[outputWire.wire]?.let { return it }

            val op = opByOutputWire[outputWire.wire] ?: error("Output wire $outputWire is not connected")
            return op.inputExpr.evaluate().also { wireOutputs[outputWire.wire] = it }
        }

        /** Evaluates the gate expression to Int, where only the lower 16 bits are used, emulating an unsigned short. */
        private fun GateExpr.evaluate(): Int = when (this) {
            is Number -> num
            is Wire   -> calcOutput(wire)
            is And    -> (a1.evaluate() and a2.evaluate()) and 0xFFFF
            is Or     -> (a1.evaluate() or a2.evaluate()) and 0xFFFF
            is Not    -> a.evaluate().inv() and 0xFFFF
            is LShift -> (a.evaluate() shl shift.evaluate()) and 0xFFFF
            is RShift -> (a.evaluate() shr shift.evaluate()) and 0xFFFF
        }
    }
}

data class LogicGateOp(val inputExpr: GateExpr, val outputWire: String) {
    sealed interface GateExpr {
        sealed interface Atom : GateExpr {
            data class Number(val num: Int) : Atom
            data class Wire(val wire: String) : Atom

            companion object {
                fun of(s: String) = s.toIntOrNull().let { num -> if (num != null) Number(num) else Wire(s) }
            }
        }

        data class And(val a1: Atom, val a2: Atom) : GateExpr
        data class Or(val a1: Atom, val a2: Atom) : GateExpr
        data class Not(val a: Atom) : GateExpr
        data class LShift(val a: Atom, val shift: Atom) : GateExpr
        data class RShift(val a: Atom, val shift: Atom) : GateExpr

        companion object {
            infix fun GateExpr.toWire(wire: String) = LogicGateOp(this, wire)
            infix fun kotlin.Number.toWire(wire: String) = Number(this.toInt()) toWire wire
            infix fun String.toWire(wire: String) = Wire(this) toWire wire
        }
    }
}

private object GateOpInputPatterns {
    val atom = Pattern.compile("(\\d+|\\w+) -> (\\w+)")
    val not = Pattern.compile("NOT (\\w+) -> (\\w+)")
    val binOp = Pattern.compile("(\\w+) (AND|OR|LSHIFT|RSHIFT) (\\w+) -> (\\w+)")
}


private fun parseLogicGateOp(s: String): LogicGateOp {
    with(GateOpInputPatterns.atom.matcher(s)) {
        if (matches()) return Atom.of(group(1)) toWire group(2)
    }
    with(GateOpInputPatterns.not.matcher(s)) {
        if (matches()) return Not(Atom.of(group(1))) toWire group(2)
    }
    with(GateOpInputPatterns.binOp.matcher(s)) {
        if (matches()) {
            val a1 = Atom.of(group((1)))
            val a2 = Atom.of(group((3)))
            return when (group(2)) {
                "AND"    -> And(a1, a2) toWire group(4)
                "OR"     -> Or(a1, a2) toWire group(4)
                "LSHIFT" -> LShift(a1, a2) toWire group(4)
                "RSHIFT" -> RShift(a1, a2) toWire group(4)
                else     -> error("Invalid gate operation $s")
            }
        }
    }
    error("Invalid gate operation $s")
}

private fun readInput(inputFileName: String): List<LogicGateOp> =
    readInputLines(2015, 7, inputFileName).map { parseLogicGateOp(it) }

private fun printResult(inputFileName: String, outputWire: String) {
    val solver = BitwiseLogicGatesCircuit()

    val ops1 = readInput(inputFileName)
    val res1 = solver.calcLogicGateOutput(ops1, outputWire)
    println("Part 1: Output for wire $outputWire: $res1")

    // For part 2, we override the input for wire "b" by the previously calculated output of wire "a" in part 1
    val ops2 = ops1.map { if (it.outputWire == "b") res1 toWire "b" else it }
    val res2 = solver.calcLogicGateOutput(ops2, outputWire)
    println("Part 2: Output for wire $outputWire: $res2")
}

fun main() {
    printResult("input.txt", "a")
}
