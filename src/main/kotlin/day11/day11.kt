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
    var input = loadInput(11).split(" ").map { it.toLong() }
//    val input = "125 17".split(" ").map { it.toLong() }

    var memo = mutableMapOf<String, Long>()

    input.sumOf {
        repeatBlinks(listOf(it), 75, memo)
    }.also { println(it) }


    return 0
}

fun repeatBlinks(nums: List<Long>, iteration: Int, memo: MutableMap<String, Long>): Long {
    if (iteration == 0) {
        return nums.size.toLong()
    }

    return nums.sumOf { num ->
        val key = "$iteration:$num"
        if(memo.contains(key)) {
            return@sumOf memo[key]!!
        }

        val newNums = blink(num)
        val res = repeatBlinks(newNums, iteration - 1, memo)

        memo[key] = res

        res
    }
}

fun blink(num: Long): List<Long> {
    return when {
        num == 0L -> listOf(1L)
        num > 0 && num.toString().length % 2 == 0 -> {
            val numString = num.toString()
            val indexToSplit = numString.length / 2
            val a = numString.substring(0, indexToSplit).toLong()
            val b = numString.substring(indexToSplit).toLong()
            listOf(a, b)
        }

        else -> listOf(num * 2024L)
    }
}