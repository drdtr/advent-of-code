package advent_of_code_2017.day18

import Util.readInputLines

object DuetAssembler {
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
    data class Snd(val expr: Expr) : Instruction
    data class Set(val register: Register, val valueExpr: Expr) : Instruction
    data class Add(val register: Register, val valueExpr: Expr) : Instruction
    data class Mult(val register: Register, val valueExpr: Expr) : Instruction
    data class Mod(val register: Register, val valueExpr: Expr) : Instruction
    data class Rcv(val register: Register) : Instruction
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
        return readInputLines(2017, 18, inputFileName).map { line -> line.split(" ", ",").filter { it.isNotEmpty() } }
                .map { tokens ->
                    when (tokens[0]) {
                        "snd" -> Snd(parseExpr(tokens[1]))
                        "set" -> Set(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "add" -> Add(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "mul" -> Mult(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "mod" -> Mod(parseRegister(tokens[1]), parseExpr(tokens[2]))
                        "rcv" -> Rcv(parseRegister(tokens[1]))
                        "jgz" -> Jump(parseExpr(tokens[1]), parseExpr(tokens[2]))
                        else  -> error("Invalid instruction \"$tokens\"")
                    }
                }
    }
}