package utils


class GridIterator<T>(private val grid: Grid<T>) : Iterator<Cell<T>> {
    private var currentRowIndex = -1
    private var currentColIndex = -1


    override fun hasNext(): Boolean {
        when {
            currentRowIndex == -1 && currentColIndex == -1 -> {
                currentColIndex = 0
                currentRowIndex = 0
            }
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
            it.forEach{ value ->
                print(value)
            }
            println()
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

fun <T> String.toGrid(mappingFunc: (rawValue: String) -> T): Grid<T> {
    val cells = this
        .lines()
        .map {
            val row = it.trim().split("").toMutableList()
            row.removeFirst()
            row.removeLast()
            row.map { rawValue -> mappingFunc(rawValue) }.toMutableList()
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

data class Cell<T>(var coord: Coord, var value: T)