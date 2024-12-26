package day15

import utils.*

val exampleInput = """
    ########
    #..O.O.#
    ##@.O..#
    #...O..#
    #.#.O..#
    #...O..#
    #......#
    ########

    <^^>>>vv<v>>v<
""".trimIndent()

class Warehouse(val grid: Grid<String>) {
    var robotPosition = grid.find { it.value == "@" }?.coord ?: throw Exception("Robot not found")
    val boxes = grid.filter { it.value == "O" }.map { it.coord }.toMutableSet()
    val walls = grid.filter { it.value == "#" }.map { it.coord }.toMutableSet()

    fun computeGPS(): Long {
        return boxes.fold(0L) { acc, boxCoord ->
            acc + (100*boxCoord.row) + boxCoord.col
        }
    }

    fun moveRobot(direction: Directions) {
        val newPosition = direction.coordVector + robotPosition

        if (!isValidPosition(newPosition)) return

        if (walls.contains(newPosition)) return

        if (!boxes.contains(newPosition)) {
            robotPosition = newPosition
            return
        }

        if (moveBox(newPosition, direction)) {
            // box was moved.
            robotPosition = newPosition
            return
        }
    }

    fun moveBox(boxPosition: Coord, direction: Directions): Boolean {
        if (!boxes.contains(boxPosition)) {
            return true
        }

        val newBoxPosition = direction.coordVector + boxPosition

        // runs into a wall.
        if (walls.contains(newBoxPosition)) {
            return false
        }

        if (!isValidPosition(newBoxPosition)) {
            return false
        }

        // is the new position occupied by boxes.
        if (!moveBox(newBoxPosition, direction)) {
            return false
        }


        boxes.remove(boxPosition)
        boxes.add(newBoxPosition)

        return true
    }

    fun isValidPosition(position: Coord): Boolean {
        return position.col in 0..grid.maxColumn && position.row in 0..grid.maxRow
    }

    fun print() {
        for (i in 0..grid.maxRow) {
            for (j in 0..grid.maxColumn) {
                val cell = Coord(i, j)
                val res = when {
                    cell == robotPosition -> "@"
                    walls.contains(cell) -> "#"
                    boxes.contains(cell) -> "O"
                    else -> "."
                }
                print(res)
            }
            println()
        }
    }

}

fun part1(): Int {
//    val input = exampleInput
    val input = loadInput(15)
    val result = input.split("\n\n")

    val grid = result[0].toGrid()
    val sequence = result[1].mapNotNull { direction ->
        when (direction) {
            '^' -> Directions.North
            '>' -> Directions.East
            'v' -> Directions.South
            '<' -> Directions.West
            else -> null
        }
    }

    val warehouse = Warehouse(grid)

//    warehouse.print()

    sequence.forEach { direction ->
        warehouse.moveRobot(direction)
//        println("Direction: $direction")
//        warehouse.print()
//        println()
    }

//    warehouse.print()

    warehouse.computeGPS().also { println(it) }

    return 0
}

fun part2(): Int {
    return 0
}
