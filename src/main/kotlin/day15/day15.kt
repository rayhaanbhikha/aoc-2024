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

val exampleInput2 = """
    #######
    #...#.#
    #.....#
    #..OO@#
    #..O..#
    #.....#
    #######

    <vv<<^^<<^^
""".trimIndent()

val exampleInput3 = """
    #######
    #.....#
    #.....#
    #.@O..#
    #..#O.#
    #...O.#
    #..O..#
    #.....#
    #######

    >><vvv>v>^^^
""".trimIndent()

val exampleInput4 = """
    ######
    #....#
    #..#.#
    #....#
    #.O..#
    #.OO@#
    #.O..#
    #....#
    ######

    <vv<<^^^
""".trimIndent()

val exampleInput5 = """
    ########
    #..O.O.#
    ##@.O..#
    #...O..#
    #.#.O..#
    #...O..#
    #......#
    ########

    <^^>>>vv<v>>v<<
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

fun part1(): Long {
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

    sequence.forEach { direction ->
        warehouse.moveRobot(direction)
    }


    return warehouse.computeGPS().also { println(it) }
}

class Warehouse2(val grid: Grid<String>) {
    var robotPosition = grid.find { it.value == "@" }?.coord ?: throw Exception("Robot not found")
    val boxes = grid.filter { it.value == "[" || it.value == "]" }.associateBy { it.coord }.toMutableMap()
    val walls = grid.filter { it.value == "#" }.map { it.coord }.toMutableSet()

    fun computeGPS(): Long {
        return boxes.values.filter { it.value == "[" }.map { it.coord }.fold(0L) { acc, boxCoord ->
//            val top = minOf(boxCoord.row, grid.maxRow - boxCoord.row)
//            val side = minOf(boxCoord.col, grid.maxColumn - boxCoord.col)


            acc + (100*boxCoord.row) + boxCoord.col
        }
    }

    fun moveRobot(direction: Directions) {
        val newPosition = direction.coordVector + robotPosition

        // within boundary.
        if (!isValidPosition(newPosition)) return

        // not in wall.
        if (walls.contains(newPosition)) return

        if (!boxes.contains(newPosition)) {
            robotPosition = newPosition
            return
        }

        if (checkBox(newPosition, direction)) {
            moveBox(newPosition, direction)
            // box was moved.
            robotPosition = newPosition
            return
        }
    }

    fun checkBox(boxPosition: Coord, direction: Directions): Boolean {
        val currentBox = boxes[boxPosition] ?: return true

        val newBoxPosition = direction.coordVector + currentBox.coord

        val isVerticalDirection = direction.name == "North" || direction.name == "South"

        if (isVerticalDirection) {
            // check 2 positions.
            val (currentBoxLeft, currentBoxRight) = when (currentBox.value) {
                "[" -> {
                    Pair(currentBox, currentBox.copy(coord = currentBox.coord + Directions.East.coordVector, value = "]"))
                }
                "]" -> {
                    Pair(currentBox.copy(coord = currentBox.coord + Directions.West.coordVector, value = "["), currentBox)
                }
                else -> throw Exception("Box value can't be anything else")
            }

            // new left and right positions.
            val (newBoxPosLeft, newBoxPosRight) = when (currentBox.value) {
                "[" -> Pair(currentBoxLeft.coord + direction.coordVector, currentBoxLeft.coord + Directions.East.coordVector + direction.coordVector)
                "]" -> Pair(currentBoxRight.coord + Directions.West.coordVector + direction.coordVector, currentBoxRight.coord + direction.coordVector)
                else -> throw Exception("Box value can't be anything else")
            }

            // runs into a wall.
            if (walls.contains(newBoxPosLeft) || walls.contains(newBoxPosRight)) {
                return false
            }

            if (!isValidPosition(newBoxPosLeft) || !isValidPosition(newBoxPosRight)) {
                return false
            }

            // is the new position occupied by boxes.
            if (!checkBox(newBoxPosLeft, direction)) {
                return false
            }

            // is the new position occupied by boxes.
            if (!checkBox(newBoxPosRight, direction)) {
                return false
            }

            return true
        }

        // runs into a wall.
        if (walls.contains(newBoxPosition)) {
            return false
        }

        if (!isValidPosition(newBoxPosition)) {
            return false
        }

        // is the new position occupied by boxes.
        if (!checkBox(newBoxPosition, direction)) {
            return false
        }

        return true
    }

    fun moveBox(boxPosition: Coord, direction: Directions): Boolean {
        val currentBox = boxes[boxPosition] ?: return true

        val newBoxPosition = direction.coordVector + currentBox.coord

        val isVerticalDirection = direction.name == "North" || direction.name == "South"

        if (isVerticalDirection) {
            // check 2 positions.
            val (currentBoxLeft, currentBoxRight) = when (currentBox.value) {
                "[" -> {
                    Pair(currentBox, currentBox.copy(coord = currentBox.coord + Directions.East.coordVector, value = "]"))
                }
                "]" -> {
                    Pair(currentBox.copy(coord = currentBox.coord + Directions.West.coordVector, value = "["), currentBox)
                }
                else -> throw Exception("Box value can't be anything else")
            }

            // new left and right positions.
            val (newBoxPosLeft, newBoxPosRight) = when (currentBox.value) {
                "[" -> Pair(currentBoxLeft.coord + direction.coordVector, currentBoxLeft.coord + Directions.East.coordVector + direction.coordVector)
                "]" -> Pair(currentBoxRight.coord + Directions.West.coordVector + direction.coordVector, currentBoxRight.coord + direction.coordVector)
                else -> throw Exception("Box value can't be anything else")
            }

            // runs into a wall.
            if (walls.contains(newBoxPosLeft) || walls.contains(newBoxPosRight)) {
                return false
            }

            if (!isValidPosition(newBoxPosLeft) || !isValidPosition(newBoxPosRight)) {
                return false
            }

            // is the new position occupied by boxes.
            if (!moveBox(newBoxPosLeft, direction)) {
                return false
            }

            // is the new position occupied by boxes.
            if (!moveBox(newBoxPosRight, direction)) {
                return false
            }

            boxes.remove(currentBoxLeft.coord)
            boxes.remove(currentBoxRight.coord)
            boxes[newBoxPosLeft] = currentBoxLeft.copy(coord = newBoxPosLeft)
            boxes[newBoxPosRight] = currentBoxRight.copy(coord = newBoxPosRight)

            return true
        }

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
        boxes[newBoxPosition] = currentBox.copy(coord = newBoxPosition)

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
                    boxes.contains(cell) -> boxes.get(cell)?.value
                    else -> "."
                }
                print(res)
            }
            println()
        }
    }

}

fun scaleUpGrid(grid: String): String {
    return grid.lines().map { row ->
        val res = row.mapNotNull { value ->
            when(value) {
                '#' -> "##"
                'O' -> "[]"
                '@' -> "@."
                '.' -> ".."
                else -> null
            }
        }
        res.joinToString("")
    }.joinToString("\n")
}

fun part2(): Int {
    val input = loadInput(15)
    val result = input.split("\n\n")


    val grid = scaleUpGrid(result[0]).toGrid()

//    grid.print()

    val sequence = result[1].mapNotNull { direction ->
        when (direction) {
            '^' -> Directions.North
            '>' -> Directions.East
            'v' -> Directions.South
            '<' -> Directions.West
            else -> null
        }
    }

    val warehouse = Warehouse2(grid)

    sequence.forEach { direction ->
        warehouse.moveRobot(direction)
    }

    warehouse.print()

    warehouse.computeGPS().also { println(it) }

    return 0
}
