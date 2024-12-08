package day7

import utils.loadInput

fun part1(): Long {
    val input = loadInput(7).lines().map {
        val result = it.split(":")
        val testValue = result[0].toLong()
        val otherNumbers = result[1].trim().split(",").map { it.split(" ").map { num -> num.toInt() } }.flatten()

        Pair(testValue, otherNumbers)
    }

    return input.filter { (testValue, numbers) ->
        // Example usage:
        val chars = listOf("+", "*")
        val length = numbers.size - 1
        val combos = combinations(chars, length)

        for (comboOp in combos) {
            val result = numbers.drop(1).zip(comboOp).fold(numbers[0].toLong()) { acc, (number, operator) ->
                val result = when (operator) {
                    "+" -> acc + number.toLong()
                    "*" -> if (acc == 0L) number.toLong() else acc * number.toLong()
                    else -> throw Exception("operator not valid $operator")
                }
                result
            }

            if (testValue == result) {
                return@filter true
            }
        }

        false
    }.sumOf { it.first }.also { println(it) }
}

fun part2(): Long {
    val input = loadInput(7).lines().map {
        val result = it.split(":")
        val testValue = result[0].toLong()
        val otherNumbers = result[1].trim().split(",").map { it.split(" ").map { num -> num.toInt() } }.flatten()

        Pair(testValue, otherNumbers)
    }

    val comboOpMapByLength = mutableMapOf<Int, List<List<String>>>()

    return input.filter { (testValue, numbers) ->
        // Example usage:
        val length = numbers.size - 1

        val combos = comboOpMapByLength.getOrElse(length) {
            val chars = listOf("||", "+", "*")
            val computedCombos = combinations(chars, length)
            comboOpMapByLength[length] = computedCombos
            computedCombos
        }

        for (comboOp in combos) {
            val result = numbers.drop(1).zip(comboOp).fold(numbers[0].toLong()) { acc, (number, operator) ->
                val result = when (operator) {
                    "+" -> acc + number.toLong()
                    "*" -> if (acc == 0L) number.toLong() else acc * number.toLong()
                    "||" -> (acc.toString() + number.toString()).toLong()
                    else -> throw Exception("operator not valid $operator")
                }
                result
            }

            if (testValue == result) {
                return@filter true
            }
        }

        false
    }
        .onEach { println(it) }
        .sumOf { it.first }.also { println(it) }
}


fun combinations(chars: List<String>, length: Int): List<List<String>> {
    if (length == 0) return listOf(emptyList())
    val result = mutableListOf<List<String>>()
    for (char in chars) {
        for (combo in combinations(chars, length - 1)) {
            result.add(listOf(char) + combo)
        }
    }
    return result
}

