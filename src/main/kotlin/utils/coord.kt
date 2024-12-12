package utils

import kotlin.math.abs

data class Coord(val row: Int, val col: Int) {
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
        return Coord(row = row*n, col = col*n)
    }

    fun greaterThan(otherCoord: Coord): Boolean {
        return otherCoord.row - row > 0 && otherCoord.col - col > 0
    }

    fun goRight(): Coord {
        return Coord(
            col =  1 * row,
            row = -1 * col
        )
    }
}