package day6

import day4.Coord
import utils.Grid
import utils.loadInput
import utils.toGrid

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


class Guard(val grid: Grid<String>) {
    val currentGuardCell = grid.find { it.value == "^" } ?: throw Exception("guard not found")
    val startingCoord = currentGuardCell.coord
    val pathWalked = mutableSetOf<Coord>()

    fun patrol() {
        while(true) {
            val result = move()
            if (!result) break
        }
    }

    fun move(): Boolean {
        pathWalked.add(currentGuardCell.coord)

        grid.updateCell(currentGuardCell.coord, "X")

        val vectorToMove =
            directions[currentGuardCell.value] ?: throw Exception("Invalid value ${currentGuardCell.value}")

        val newCoord = Coord(
            row = currentGuardCell.coord.row + vectorToMove.row,
            col = currentGuardCell.coord.col + vectorToMove.col,
        )

        if (newCoord.row !in 0..grid.maxRow || newCoord.col !in 0..grid.maxColumn) {
            return false
        }

        if (grid.cells[newCoord.row][newCoord.col] == "#") {
            // rotate.
            currentGuardCell.value = followOnDirection[currentGuardCell.value] ?: throw Exception("foo")
            return move()
        }

        currentGuardCell.coord = newCoord

        grid.updateCell(currentGuardCell.coord, currentGuardCell.value)


        return true
    }


    fun isMovingInLoop(): Boolean {
        currentGuardCell.coord = startingCoord
        currentGuardCell.value = "^"




        return false
    }


}

fun part1(): Int {
    val grid = loadInput(6).toGrid()

    val guard = Guard(grid)

    while(guard.move()) {}

    return grid.map { if (it.value == "X") 1 else 0 }.sum().also { println(it) }
}

fun part2(): Int {
    val grid = loadInput(6, example = true).toGrid()

    val guard = Guard(grid)

    guard.patrol()

    println(guard.pathWalked)
    guard.pathWalked.drop(0).forEach {

    }

    println(guard.startingCoord)

    // once guard has walked path.

    // we should retrace steps. At each point put an obstacle and check if the guard enters a loop.

    return 0
}