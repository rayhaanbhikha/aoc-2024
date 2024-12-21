package day11

import utils.loadInput

fun part1(): Int {
    var input = loadInput(11).split(" ").map { it.toLong() }

    for (i in 0..<25) {
        input = input.map {
            when {
                it == 0L -> listOf(1L)
                it > 0 && it.toString().length % 2 == 0 -> {
                    val numString = it.toString()
                    val indexToSplit = numString.length / 2
                    val a = numString.substring(0, indexToSplit).toLong()
                    val b = numString.substring(indexToSplit).toLong()
                    listOf(a, b)
                }
                else -> listOf(it * 2024L)
            }
        }.flatten()
//            .also { println(it) }

    }

    println(input.size)

    return 0
}

fun part2(): Int {
    return 0
}
