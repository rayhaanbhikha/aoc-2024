package day2

import utils.loadInput
import kotlin.math.abs

fun part1(): Int {
    return loadInput(2, true)
        .lines()
        .map { it ->
            val levels = it.split(" ").map { num -> num.toInt() }

            print("$levels -> ${validateRow(levels)}\n")

            if (validateRow(levels)){
                1
            } else {
                0
            }
        }.sum()
}

fun part2(): Int {
    return loadInput(2, example = false)
        .lines()
        .map {
            val levels = it.split(" ").map { num -> num.toInt() }

            if (validateRow2(levels)){
                1
            } else {
                0
            }
        }.sum()
}

private fun validateRow2(levels: List<Int>): Boolean {
    var delta: Int? = null

    var errors = false

    for (i in 0..<levels.size - 1) {
        val a = levels[i]
        val b = levels[i + 1]

        val diff = a - b

        delta = when {
            delta != null -> delta
            a < b -> -1
            a > b -> 1
            else -> 0
        }

        val res = when {
            a < b && delta == 1 -> {
                false
            }
            a > b && delta == -1 -> {
                false
            }
            abs(diff) !in 1..3 -> {
                false
            }
            else -> true
        }

        if (!res) {
            errors = true
        }
        continue
    }

    if (errors) {
        for(i in levels.indices) {
            val ml = levels.toMutableList()
            ml.removeAt(i)
            if (validateRow(ml)) {
                return true
            }
        }
        return false
    }

    return true
}

private fun validateRow(levels: List<Int>): Boolean {
    var delta: Int? = null

    for (i in 0..<levels.size - 1) {
        val a = levels[i]
        val b = levels[i + 1]

        val diff = a - b

        if (delta == null) {
            delta = when {
                a < b -> -1
                a > b -> 1
                else -> 0
            }
        }

        when {
            a < b && delta == 1 -> {
                return false
            }
            a > b && delta == -1 -> {
                return false
            }
            abs(diff) !in 1..3 -> {
                return false
            }
        }
    }

    return true
}

