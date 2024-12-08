package utils

import java.io.File


fun loadInput(day: Int, example: Boolean = false): String {
    var prefix = "./inputs/day${day}"

    prefix += if (!example) {
        "_input.txt"
    } else {
        "_input_example.txt"
    }

    return File(prefix).readText().trim()
}

data class Cell<T>(var coord: Coord, var value: T)

class GridIterator<T>(private val grid: Grid<T>) : Iterator<Cell<T>> {
    private var currentRowIndex = 0
    private var currentColIndex = 0


    override fun hasNext(): Boolean {
        when {
            currentColIndex == grid.maxColumn -> {
                currentColIndex = 0
                currentRowIndex++
            }

            else -> {
                currentColIndex++
            }
        }

        return currentColIndex in 0..grid.maxColumn && currentRowIndex in 0..grid.maxRow
    }

    override fun next(): Cell<T> {
        return Cell(
            coord = Coord(
                row = currentRowIndex,
                col = currentColIndex
            ),
            value = grid.cells[currentRowIndex][currentColIndex]
        )
    }

}

data class Grid<T>(val cells: MutableList<MutableList<T>>) : Iterable<Cell<T>> {
    val maxColumn = cells.maxCol()
    val maxRow = cells.maxRow()

    override fun iterator(): Iterator<Cell<T>> = GridIterator(this)

    fun updateCell(coord: Coord, newValue: T) {
        // TODO: validate.
        cells[coord.row][coord.col] = newValue
    }

    fun print() {
        println()
        cells.forEach{
            println(it)
        }
    }

}

fun String.toGrid(): Grid<String> {
    val cells = this
        .lines()
        .map {
            val row = it.trim().split("").toMutableList()
            row.removeFirst()
            row.removeLast()
            row
        }
        .toMutableList()

    return Grid(cells)
}

fun <T> List<List<T>>.maxCol(): Int {
    return this[0].size - 1
}

fun <T> List<List<T>>.maxRow(): Int {
    return this.size - 1
}


data class Coord(val row: Int, val col: Int) {
    fun add(otherCoord: Coord): Coord {
        return Coord(row = row + otherCoord.row, col = col + otherCoord.col)
    }

    fun goRight(): Coord {
        return Coord(
            col =  1 * row,
            row = -1 * col
        )
    }
}

fun rotateRight(x: Int, y: Int): Pair<Int, Int> {
    // Rotation matrix for 90 degrees clockwise:
    // [  0,  1 ]
    // [ -1,  0 ]

    val newX = 0 * x + 1 * y  // This simplifies to y
    val newY = -1 * x + 0 * y // This simplifies to -x

    return Pair(newX, newY)
}
