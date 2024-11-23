package advent_of_code_2017.day23

import Util.readInputLines

object CoprocessorAssembler {
    @JvmInline
    value class Registers(val registers: Map<Char, Long>) {
        companion object {
            fun registersOf(vararg registerValues: Pair<Char, Long>) = Registers(mapOf(*registerValues))
        }
    }

    sealed interface Expr
    data class Constant(val value: Int) : Expr
    data class Register(val name: Char) : Expr

    sealed interface Instruction
    data class Set(val register: Register, val valueExpr: Expr) : Instruction
    data class Sub(val register: Register, val valueExpr: Expr) : Instruction
    data class Mult(val register: Register, val valueExpr: Expr) : Instruction
    data class Jump(val conditionExpr: Expr, val offsetExpr: Expr) : Instruction

    fun readInput(inputFileName: String): List<Instruction> {
        fun parseRegister(s: String) = if (s.length == 1) {
            Register(s[0])
        } else {
            error("Expected integer constant or one-letter register name but was $s")
        }

        fun parseExpr(s: String) = try {
            Constant(s.toInt())
        } catch (e: NumberFormatException) {
            parseRegister(s)
        }
        return readInputLines(2017, 23, inputFileName).map { line -> line.split(" ", ",").filter { it.isNotEmpty() } }
                .map { tokens ->
                    when (tokens[0]) {
                        "set" -> Set(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "sub" -> Sub(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "mul" -> Mult(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "jnz" -> Jump(parseExpr(tokens[1]), parseExpr(tokens[2]))
                        else  -> error("Invalid instruction \"$tokens\"")
                    }
                }
    }
}