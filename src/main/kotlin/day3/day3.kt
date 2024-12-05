package day3

import utils.loadInput

fun part1(): Int {
    val input = loadInput(3)

    val reg = Regex("mul\\((?<x>\\d+),(?<y>\\d+)\\)")

    val results = reg.findAll(input)

    return results.map { it.groupValues }
        .map {
            it[1].toInt() * it[2].toInt()
        }.sum().also { println(it) }
}

fun part2(): Int {
    val input = loadInput(3)
//    val input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    val reg = Regex("(don't\\(\\))|(do\\(\\))|mul\\((?<x>\\d+),(?<y>\\d+)\\)")

    val results = reg.findAll(input)

    var ignoreMul = false

    return results
        .map { it.groupValues }
        .map {
            val hasDont = it[1] != ""
            val hasDo = it[2] != ""

            if (hasDont) {
                ignoreMul = true
            }

            if (hasDo) {
                ignoreMul = false
            }

            if (it[0] == "don't()" || it[0] == "do()") return@map 0

            if (!ignoreMul) it[3].toInt() * it[4].toInt() else 0
        }.sum().also { println(it) }
}
