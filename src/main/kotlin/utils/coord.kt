package utils

import kotlin.math.abs

data class Coord(val row: Int, val col: Int) {

    override fun toString(): String {
        return "$row,$col"
    }

    operator fun plus(otherCoord: Coord): Coord {
        return Coord(row = row + otherCoord.row, col = col + otherCoord.col)
    }

    operator fun minus(otherCoord: Coord): Coord {
        return Coord(row = row - otherCoord.row, col = col - otherCoord.col)
    }

    fun diff(otherCoord: Coord): Coord {
        return Coord(row = row - otherCoord.row, col = col - otherCoord.col)
    }

    operator fun times(n: Int): Coord {
        return Coord(row = row * n, col = col * n)
    }

    fun greaterThan(otherCoord: Coord): Boolean {
        return otherCoord.row - row > 0 && otherCoord.col - col > 0
    }

    fun goRight(): Coord {
        return Coord(
            col = 1 * row,
            row = -1 * col
        )
    }

    fun cardinalNeighbours(): List<Coord> {
        return Directions.entries.filter { !it.isDiagonal }.map { it.coordVector + this }
    }

    fun intercardinalNeighbours(): List<Coord> {
        return Directions.entries.map { it.coordVector + this }
    }

    fun areNeighbours(otherCoord: Coord) =
        (abs(row - otherCoord.row) == 1 && abs(col - otherCoord.col) == 0) || (abs(row - otherCoord.row) == 0 && abs(col - otherCoord.col) == 1)
}

fun Coord.inValidRange(rowRange: IntRange, colRange: IntRange): Boolean {
    return rowRange.contains(this.row) && colRange.contains(this.col)
}

enum class Directions(val coordVector: Coord, val isDiagonal: Boolean = false) {
    North(Coord(-1, 0)),
    NorthEast(Coord(-1, 1), true),
    East(Coord(0, 1)),
    SouthEast(Coord(1, 1), true),
    South(Coord(1, 0)),
    SouthWest(Coord(1, -1), true),
    West(Coord(0, -1)),
    NorthWest(Coord(-1, -1), true)
}