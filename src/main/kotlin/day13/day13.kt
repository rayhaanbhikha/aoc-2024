package day13

import utils.loadInput
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.round


val exampleInput1 = """
    Button A: X+94, Y+34
    Button B: X+22, Y+67
    Prize: X=8400, Y=5400

    Button A: X+26, Y+66
    Button B: X+67, Y+21
    Prize: X=12748, Y=12176

    Button A: X+17, Y+86
    Button B: X+84, Y+37
    Prize: X=7870, Y=6450

    Button A: X+69, Y+23
    Button B: X+27, Y+71
    Prize: X=18641, Y=10279
""".trimIndent()

data class Equation(val a: List<List<Long>>, var b: List<Long>) {

    // ad-bc
    fun determinant() = a[0][0]*a[1][1]-a[1][0]*a[0][1]

    fun inverseOfDeterminant() = 1 / determinant().toFloat()

    fun solve(): List<Float>? {
        val invDet = inverseOfDeterminant()
        val aInv = listOf(
            listOf(a[1][1], -1*a[0][1]),
            listOf(-1 * a[1][0], a[0][0])
        )
        val aInverse = run {
            aInv.map { row ->
                row.map { value -> value * invDet }
            }
        }

        val x = round(aInverse[0][0]*b[0] + aInverse[0][1] * b[1])
        val y = round( aInverse[1][0]*b[0] + aInverse[1][1] * b[1])

        val aSol = x*a[0][0] + y*a[0][1]
        val bSol = x*a[1][0] + y*a[1][1]

        if (aSol.toLong() != b[0] && bSol.toLong() != b[1]) {
            return null
        }

        return listOf(
            x,
            y
        )
    }

    fun solve2(): List<BigDecimal>? {
        val newB = listOf(
            (b[0] + 10000000000000).toBigDecimal(),
            (b[1] + 10000000000000).toBigDecimal()
        )
        val invDet = 1.toBigDecimal().divide(determinant().toBigDecimal(), MathContext.DECIMAL128)
        val aInv = listOf(
            listOf(a[1][1], -1*a[0][1]),
            listOf(-1 * a[1][0], a[0][0])
        )
        val aInverse = run {
            aInv.map { row ->
                row.map { value -> value.toBigDecimal().times(invDet) }
            }
        }

        val x = aInverse[0][0].times(newB[0]).plus(aInverse[0][1].times(newB[1])).round(MathContext.DECIMAL64)
        val y = aInverse[1][0].times(newB[0]).plus(aInverse[1][1].times(newB[1])).round(MathContext.DECIMAL64)

        if (x.hasDecimalPart() || y.hasDecimalPart()) {
            return null
        }

        return listOf(
            x,
            y
        )
    }

}

fun BigDecimal.hasDecimalPart(): Boolean {
    // Check if the remainder when divided by 1 is non-zero (i.e., has numbers after the decimal point)
    return this.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0;
}

val buttonReg = Regex("Button \\D: X\\+(?<x>\\d+), Y\\+(?<y>\\d+)", RegexOption.MULTILINE)
val prizeReg = Regex("Prize: X=(?<x>\\d+), Y=(?<y>\\d+)", RegexOption.MULTILINE)

fun part1(): Int {
//    val input = exampleInput1
    val input = loadInput(13)

    val equations = parseInput(input)

    equations.mapNotNull {
        val res = it.solve() ?: return@mapNotNull null
        if (res[0] > 100 || res[1] > 100) return@mapNotNull null
        res[0]*3 + res[1] * 1
    }.sum().also { println(it) }

    return 0
}

fun parseInput(input: String): List<Equation> {
    return input.split("\n\n").map {

        val buttonRegResult = buttonReg.findAll(it)
        val prizeRegResult = prizeReg.findAll(it)

        val buttons = buttonRegResult.asIterable().map { match ->
            val x = match.groups["x"]?.value?.toLong()!!
            val y = match.groups["y"]?.value?.toLong()!!

            Pair(x, y)
        }

        val prize = prizeRegResult.asIterable().map { match ->
            val x = match.groups["x"]?.value?.toLong()!!
            val y = match.groups["y"]?.value?.toLong()!!

            listOf(x, y)
        }.take(1).flatten()

        val a = listOf(
            listOf(buttons[0].first, buttons[1].first),
            listOf(buttons[0].second, buttons[1].second)
        )

        Equation(a = a, b= prize)
    }
}

fun part2(): Int {

//        val input = exampleInput1
    val input = loadInput(13)

    val equations = parseInput(input)

    equations.fold(0.toBigDecimal()){acc, equation ->
        val res = equation.solve2() ?: return@fold acc
        acc.plus(res[0].times(3.toBigDecimal()) + res[1].times(1.toBigDecimal()))
    }.also { println(it) }


    return 0
}
