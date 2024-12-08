package utils

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