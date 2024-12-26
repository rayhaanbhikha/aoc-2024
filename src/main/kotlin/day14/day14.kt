package day14

import utils.Coord
import utils.loadInput
import kotlin.math.max

class Robot(val initialPos: Coord, val velocity: Coord) {
    var currentPos = initialPos

    fun move(maxRow: Int, maxCol: Int) {
        currentPos += velocity
        val newRow = currentPos.row + when {
            currentPos.row < 0 -> maxRow + 1
            currentPos.row > maxRow -> -(maxRow + 1)
            else -> 0
        }
        val newCol = currentPos.col + when {
            currentPos.col < 0 -> (maxCol + 1)
            currentPos.col > maxCol -> -(maxCol + 1)
            else -> 0
        }

        currentPos = currentPos.copy(row = newRow, col = newCol)
    }

    fun computeQuadrant(maxRow: Int, maxCol: Int): Int {
        return when {
            currentPos.row in 0..<maxRow / 2 && currentPos.col in 0..<maxCol / 2 -> 1
            currentPos.row in 0..<maxRow / 2 && currentPos.col in maxCol / 2 + 1..maxCol -> 2
            currentPos.row in maxRow / 2 + 1..maxRow && currentPos.col in 0..<maxCol / 2 -> 3
            currentPos.row in maxRow / 2 + 1..maxRow && currentPos.col in maxCol / 2 + 1..maxCol -> 4
            else -> 0
        }
    }
}

val reg = Regex("p=(?<xcoord>\\d+),(?<ycoord>\\d+) v=(?<xvec>-*\\d+),(?<yvec>-*\\d+)")

fun part1(): Long {
    val maxRow = 102
    val maxCol = 100

    val robots = loadInput(14).lines().mapNotNull {
        val res = reg.find(it) ?: return@mapNotNull null
        val x = res.groups["xcoord"]?.value?.toInt() ?: return@mapNotNull null
        val y = res.groups["ycoord"]?.value?.toInt() ?: return@mapNotNull null
        val vecX = res.groups["xvec"]?.value?.toInt() ?: return@mapNotNull null
        val vecY = res.groups["yvec"]?.value?.toInt() ?: return@mapNotNull null
        Robot(Coord(y, x), Coord(vecY, vecX))
    }


    for (i in 0..<100) {
        robots.forEach { it.move(maxRow, maxCol) }
    }

    return robots
        .groupBy { it.computeQuadrant(maxRow, maxCol) }
        .mapValues { it.value.size.toLong() }
        .filter { it.key != 0 }
        .values.reduce { acc, i -> i * acc }
}

fun part2(): Int {
    return 0
}
