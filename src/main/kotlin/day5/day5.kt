package day5

import utils.loadInput

fun part1(): Int {
    val res = loadInput(5).lines()

    val (rules, updates) = res.filter { it != "" }.partition { it.contains("|") }

    val rulesMap = rules
        .map {
            val r = it.split("|")
            Pair(r[0].toInt(), r[1].toInt())
        }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        ).mapValues {
            it.value.toSet()
        }

    val listOfUpdates = updates.map { it.split(",").map { num -> num.toInt() } }

    return listOfUpdates.filter { checkUpdatesAreInOrder(it, rulesMap) }.sumOf { it[it.size / 2] }.also { println(it) }
}

fun part2(): Int {
    val res = loadInput(5).lines()

    val (rules, updates) = res.filter { it != "" }.partition { it.contains("|") }

    val rulesMap = rules
        .map {
            val r = it.split("|")
            Pair(r[0].toInt(), r[1].toInt())
        }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        ).mapValues {
            it.value.toSet()
        }

    val listOfUpdates = updates.map { it.split(",").map { num -> num.toInt() } }


    return listOfUpdates
        .filterNot { checkUpdatesAreInOrder(it, rulesMap) }
        .map {
            it.sortedWith({ a, b ->
                // check a.
                val numsAfterA = rulesMap[a]
                val numsAfterB = rulesMap[b]

                when {
                    numsAfterA?.contains(b) == true -> -1
                    numsAfterB?.contains(a) == true -> 1
                    else -> 0
                }
            })
        }
        .sumOf { it[it.size / 2] }
        .also { println(it) }
}

fun checkUpdatesAreInOrder(updates: List<Int>, rulesMap: Map<Int, Set<Int>>): Boolean {
    for (i in 0..<updates.size - 1) {
        val a = updates[i]
        val b = updates[i + 1]

        val numsAfterA = rulesMap[a]
        val numsAfterB = rulesMap[b]

        when {
            numsAfterA?.contains(b) == false -> {
                return false
            }

            numsAfterB?.contains(a) == true -> {
                return false
            }
        }
    }

    return true
}

