package day12

import utils.*


class GardenPlot(val id: String, val coord: Coord) {
    var boundaries: Set<String> = setOf()

    fun computePerimeter(grid: Grid<GardenPlot>): Int {
        return coord.cardinalNeighbours()
            .asSequence()
            .map { neighbourCellCoord ->
                val inValidRange = neighbourCellCoord.inValidRange(0..grid.maxRow, 0..grid.maxColumn)
                if (!inValidRange) return@map 1
                val neighbourCell = grid.cells[neighbourCellCoord.row][neighbourCellCoord.col]
                if (neighbourCell.id != id) return@map 1
                0
            }
            .sum()
    }

    fun walk(grid: Grid<GardenPlot>, coordsVisited: MutableSet<Coord> = mutableSetOf()) {
        if (coordsVisited.contains(this.coord)) return

        coordsVisited.add(this.coord)

        val neighbours = coord
            .cardinalNeighbours().filter { it.inValidRange(0..grid.maxRow, 0..grid.maxColumn) }
            .map { Cell(coord = Coord(it.row, it.col), value = grid.cells[it.row][it.col]) }
            .filter { it.value.id == id }

        if (neighbours.isEmpty()) return

        neighbours.forEach { it.value.walk(grid, coordsVisited) }

        return
    }

    fun setBoundaries(grid: Grid<GardenPlot>) {
        this.boundaries = coord
            .cardinalNeighbours()
            .mapNotNull { neighbourCellCoord ->
                val boundary = when {
                    neighbourCellCoord.col == coord.col -> "row:$id:${coord.row}:${neighbourCellCoord.row}:"
                    neighbourCellCoord.row == coord.row -> "col:$id:${coord.col}:${neighbourCellCoord.col}"
                    else -> throw Exception("not possible")
                }
                val inValidRange = neighbourCellCoord.inValidRange(0..grid.maxRow, 0..grid.maxColumn)
                if (!inValidRange) return@mapNotNull boundary

                val neighbourCell = grid.cells[neighbourCellCoord.row][neighbourCellCoord.col]
                if (neighbourCell.id != id) return@mapNotNull boundary

                null
            }.toSet()
    }

    fun computePerimeter2(grid: Grid<GardenPlot>, boundariesVisited: MutableSet<String>): Long {
        return boundaries.sumOf {
            if (boundariesVisited.contains(it)) {
                // boundary has been visited. But is this an isolated node?
                when {
                    it.contains("row") -> {
                        val left = grid.get(coord + Directions.West.coordVector)
                        if (left == null || left.id != id || !left.boundaries.contains(it)) {
                            return@sumOf 1
                        }
                    }

                    it.contains("col") -> {
                        val up = grid.get(coord + Directions.North.coordVector)
                        if (up == null || up.id != id || !up.boundaries.contains(it)) {
                            return@sumOf 1
                        }
                    }

                    else -> throw Exception("not possible")
                }

                return@sumOf 0L
            } else {
                boundariesVisited.add(it)
                return@sumOf 1L
            }
        }
    }

}

fun part1(): Int {
    val input = loadInput(12).toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//        OOOOO
//        OXOXO
//        OOOOO
//        OXOXO
//        OOOOO
//    """.trimIndent().toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//        AAAA
//        BBCD
//        BBCC
//        EEEC
//    """.trimIndent().toGrid{ rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//RRRRIICCFF
//RRRRIICCCF
//VVRRRCCFFF
//VVRCCCJFFF
//VVVVCJJCFE
//VVIVCCJJEE
//VVIIICJJEE
//MIIIIIJJEE
//MIIISIJEEE
//MMMISSJEEE
//    """.trimIndent().toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }

    // need to group the plant pots by neighbours.


    val coordsVisited = mutableSetOf<Coord>()
    val cellsToSearch = input.toMutableList()
    val gardenPlotNeighbours = mutableMapOf<String, List<GardenPlot>>()

    while (cellsToSearch.isNotEmpty()) {
        val currentCell = cellsToSearch.removeFirst()
        if (coordsVisited.contains(currentCell.coord)) continue

        val gardenPlotsVisited = mutableSetOf<Coord>()
        currentCell.value.walk(grid = input, gardenPlotsVisited)

        coordsVisited.add(currentCell.coord)
        coordsVisited.addAll(gardenPlotsVisited)

        val key = "${currentCell.value.id}:${gardenPlotsVisited.size}:${currentCell.coord}"
        gardenPlotNeighbours[key] = gardenPlotsVisited.map { input.cells[it.row][it.col] }
    }


    gardenPlotNeighbours.mapValues {
        val totalPerimeter = it.value.fold(0L) { acc, cell -> cell.computePerimeter(input) + acc }
        it.value.size * totalPerimeter
    }
        .values.sum().also { println(it) }

    return 0
}

fun part2(): Int {
    val input = loadInput(12).toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//    EEEEE
//    EXXXX
//    EEEEE
//    EXXXX
//    EEEEE
//    """.trimIndent().toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//        AAAA
//        BBCD
//        BBCC
//        EEEC
//    """.trimIndent().toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//OOOOO
//OXOXO
//OOOOO
//OXOXO
//OOOOO
//    """.trimIndent().toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//    AAAAAA
//    AAABBA
//    AAABBA
//    ABBAAA
//    ABBAAA
//    AAAAAA
//    """.trimIndent().toGrid{ rawValue, coord -> GardenPlot(rawValue, coord) }
//    val input = """
//RRRRIICCFF
//RRRRIICCCF
//VVRRRCCFFF
//VVRCCCJFFF
//VVVVCJJCFE
//VVIVCCJJEE
//VVIIICJJEE
//MIIIIIJJEE
//MIIISIJEEE
//MMMISSJEEE
//    """.trimIndent().toGrid { rawValue, coord -> GardenPlot(rawValue, coord) }

    // need to group the plant pots by neighbours.

    input.forEach { it.value.setBoundaries(input) }

    val coordsVisited = mutableSetOf<Coord>()
    val cellsToSearch = input.toMutableList()
    val gardenPlotNeighbours = mutableMapOf<String, List<GardenPlot>>()

    while (cellsToSearch.isNotEmpty()) {
        val currentCell = cellsToSearch.removeFirst()
        if (coordsVisited.contains(currentCell.coord)) continue

        val gardenPlotsVisited = mutableSetOf<Coord>()
        currentCell.value.walk(grid = input, gardenPlotsVisited)

        coordsVisited.add(currentCell.coord)
        coordsVisited.addAll(gardenPlotsVisited)

        val key = "${currentCell.value.id}:${gardenPlotsVisited.size}:${currentCell.coord}"
        gardenPlotNeighbours[key] = gardenPlotsVisited.map { input.cells[it.row][it.col] }
    }


    val boundaries = mutableSetOf<String>()
    gardenPlotNeighbours.mapValues {
        val sortedGardenPlots = it.value.sortedWith(compareBy({ it.coord.row }, { it.coord.col }))
        val totalPerimeter = sortedGardenPlots.fold(0L) { acc, cell -> cell.computePerimeter2(input, boundaries) + acc }
        val res = it.value.size * totalPerimeter
        res
    }
        .values.sum().also { println(it) }

    return 0
}
