package day12

import utils.*

class GardenPlot(val id: String, private val coord: Coord) {
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
    return 0
}
