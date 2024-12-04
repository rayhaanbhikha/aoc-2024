package day1

import utils.loadInput
import kotlin.math.abs

fun part1(): Int {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    loadInput(1)
        .lines()
        .forEach { row ->
            val result = row.split(" ").filter {
                it != ""
            }
            list1.add(result[0].toInt())
            list2.add(result[1].toInt())
        }

    list1.sort()
    list2.sort()

    return list1.zip(list2).sumOf { it ->
        abs(it.first - it.second)
    }
}

fun part2(): Int {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    loadInput(1)
        .lines()
        .forEach { row ->
            val result = row.split(" ").filter {
                it != ""
            }
            list1.add(result[0].toInt())
            list2.add(result[1].toInt())
        }

    val occurenceMap = list2.groupBy { it }

    return list1.sumOf {
        val simScore = occurenceMap[it]?.size ?: 0
        it * simScore
    }
}