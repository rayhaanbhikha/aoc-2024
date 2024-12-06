package day4

import utils.loadInput
import utils.maxCol
import utils.maxRow


data class Coord(val row: Int, val col: Int)

class Cell(val coord: Coord,val maxRow: Int,val maxCol: Int, minSearch: Int = 0, maxSearch: Int = 0) {
    private val range = minSearch..maxSearch
    val n = range.map{ coord.copy(row = coord.row - it) }.filter { validate(it) }
    val ne = range.map{ coord.copy(row = coord.row - it,col = coord.col + it) }.filter { validate(it) }
    val e = range.map{ coord.copy(col = coord.col + it) }.filter { validate(it) }
    val se = range.map{ coord.copy(row = coord.row + it, col = coord.col + it) }.filter { validate(it) }
    val s = range.map{ coord.copy(row = coord.row + it) }.filter { validate(it) }
    val sw = range.map{ coord.copy(row = coord.row + it, col = coord.col - it) }.filter { validate(it) }
    val w = range.map{ coord.copy(col = coord.col - it) }.filter { validate(it) }
    val nw = range.map{ coord.copy(row = coord.row - it, col = coord.col - it) }.filter { validate(it) }

    val neighbours = listOf(n, ne, e, se, s, sw, w, nw)
    val diagonals = listOf(ne, se)

    fun validate(coord: Coord): Boolean {
        return coord.col in 0..maxCol && coord.row in 0..maxRow
    }

    fun onNeighbours(cb: (coords: List<Coord>) -> Unit) {
        neighbours.forEach { cb(it) }
    }

    fun onDiagonals(cb: (coords: List<Coord>) -> Unit) {
        diagonals.forEach { cb(it) }
    }
}

fun part1(): Int {
    val input = loadInput(4).lines().map { rawRow ->
        val row = rawRow.split("").toMutableList()
        row.removeAt(0)
        row.removeAt(row.size - 1)
        row.toList()
    }

    var count = 0

    for (i in 0..input.maxRow()) {
        for(j in 0..input.maxCol()) {
            val cell = Cell(coord = Coord(i, j), input.maxRow(), input.maxCol(), 0, 3)
            cell.onNeighbours { coords ->
                val res = coords.joinToString(separator = "") { input[it.row][it.col] }
                if (res.lowercase() == "xmas") {
                    count++
                }
            }
        }
    }

    return count
}


fun part2(): Int {
    val input = loadInput(4).lines().map { rawRow ->
        val row = rawRow.split("").toMutableList()
        row.removeAt(0)
        row.removeAt(row.size - 1)
        row.toList()
    }


    var count = 0

    for (i in 0..input.maxRow()) {
        for(j in 0..input.maxCol()) {
            val cell = Cell(coord = Coord(i, j), input.maxRow(), input.maxCol(), -1, 1)

            val res1 = cell.ne.joinToString(separator = "") { input[it.row][it.col] }.lowercase()
            val res2 = cell.nw.joinToString(separator = "") { input[it.row][it.col] }.lowercase()

            val e = (res1 == "mas" || res1 == "sam") && (res2 == "mas" || res2 == "sam")

            if(e) {
                count++
            }
        }
    }

    return count
}


