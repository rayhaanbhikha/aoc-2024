package day6

import jdk.jfr.Enabled
import utils.*

val directions = mapOf(
    "^" to Coord(-1, 0),
    ">" to Coord(0, 1),
    "v" to Coord(1, 0),
    "<" to Coord(0, -1)
)

val followOnDirection = mapOf(
    "^" to ">",
    ">" to "v",
    "v" to "<",
    "<" to "^"
)

data class GuardHistory(var coord: Coord, val value: String, val count: Int)

class Guard(val grid: Grid<String>, startingCoord: Coord, startingValue: String) {
    val currentGuardCell = Cell(value = startingValue, coord = startingCoord)
    val pathWalked = mutableListOf<GuardHistory>()

    fun patrol(): List<GuardHistory> {
        var count = 0
        while (true) {
            val result = move()
            if (!result) break
            val newGuardHistory = GuardHistory(currentGuardCell.coord, currentGuardCell.value, count)
            pathWalked.add(newGuardHistory)
            count++
        }

        return pathWalked
    }

    fun patrolWithLoopDetection(debugEnabled: Boolean = false): Boolean {
        var count = 0
        while (true) {
            val result = move()
            if (!result) break
            val newGuardHistory = GuardHistory(currentGuardCell.coord, currentGuardCell.value, count)
            if (debugEnabled) {
                grid.updateCell(coord = newGuardHistory.coord, newValue = newGuardHistory.value)
                grid.print()
            }

            if (pathWalked.find { it.coord == newGuardHistory.coord && it.value == newGuardHistory.value } != null) {
                return true
            }

            pathWalked.add(newGuardHistory)
            count++
        }

        if (debugEnabled) {
            clean()
        }

        return false
    }

    private fun move(): Boolean {
        val vectorToMove =
            directions[currentGuardCell.value] ?: throw Exception("Invalid value ${currentGuardCell.value}")

        val newCoord = currentGuardCell.coord.add(vectorToMove)

        if (newCoord.row !in 0..grid.maxRow || newCoord.col !in 0..grid.maxColumn) {
            // outside of grid.
            return false
        }
        val cellValue = grid.cells[newCoord.row][newCoord.col]
        if (cellValue == "#" || cellValue == "o") {
            // rotate.
            currentGuardCell.value = followOnDirection[currentGuardCell.value] ?: throw Exception("foo")
            return move()
        }

        currentGuardCell.coord = newCoord

        return true
    }

    private fun clean() {
        for (i in 0..grid.maxRow) {
            for (j in 0..grid.maxColumn) {
                val cellValue = grid.cells[i][j]
                if (cellValue != "#") {
                    grid.cells[i][j] = "."
                }
            }
        }
    }
}

fun part1(): Int {
    val grid = loadInput(6, example = true).toGrid()
    val guardStartingCell = grid.find { it.value == "^" } ?: throw Exception("guard not found")

    val guard = Guard(grid, guardStartingCell.coord, guardStartingCell.value)

    guard.patrol()

    return guard.pathWalked.map { it.coord }.toSet().size.also { println(it) }
}

fun part2(): Int {
    val grid = loadInput(6).toGrid()
    val guardStartingCell = grid.find { it.value == "^" } ?: throw Exception("guard not found")

    val guard = Guard(grid, guardStartingCell.coord, guardStartingCell.value)

    guard.patrol()

    val pathToSearch = guard.pathWalked.drop(0)

    val loopsDetectedWithCoords = mutableListOf<GuardHistory>()
    var loopsDetected = 0

    pathToSearch.toSet().forEach { guardPathStep ->
        // simulate a block.

        grid.updateCell(guardPathStep.coord, "o")

        // new guard
        val newGuard = Guard(grid, guardStartingCell.coord, guardStartingCell.value)
        val hasLoop = newGuard.patrolWithLoopDetection(false)


        if (hasLoop) {
            loopsDetectedWithCoords.add(guardPathStep)
            loopsDetected++
        }


        grid.updateCell(guardPathStep.coord, ".")
    }

    println(loopsDetected)
    loopsDetectedWithCoords.map { it.coord }.toSet().size.also { println(it) }

    return 0
}
