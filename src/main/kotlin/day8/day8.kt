package day8

import utils.*

fun part1(): Int {
    val grid = loadInput(8).toGrid()
    val antiNodesMap = grid.filter { it.value != "." }.groupBy { it.value }

    var count = 0

    antiNodesMap.forEach { antennaCoord ->
        println("${antennaCoord.key} -> ${antennaCoord.value}")

        computeUniqueCoordPairs(antennaCoord.value)
            .map { (coord1, coord2) ->
                val diffVector = coord1.diff(coord2)

                val an2 = coord2 + Coord(row = -1 * diffVector.row, col = -1 * diffVector.col)
                val an1 = coord1 + diffVector

                listOf(an1, an2)
            }
            .flatten()
            .filter { antiNodeCoord ->
                antiNodeCoord.col in 0..grid.maxColumn && antiNodeCoord.row in 0..grid.maxRow
            }.forEach { antiNodeCoord ->
                    count++
                    grid.updateCell(antiNodeCoord, "#")
            }
    }

    grid.filter { it.value == "#" }.size.also { println(it) }

    return 0
}

fun part2(): Int {
    val grid = loadInput(8).toGrid()
//    val grid = loadInput("day8_input_example_3.txt").toGrid()

    val antiNodesMap = grid.filter { it.value != "." }.groupBy { it.value }

    var count = 0

    antiNodesMap.forEach { antennaCoord ->
        computeUniqueCoordPairs(antennaCoord.value)
            .map { (coord1, coord2) ->
                val diffVector = coord1.diff(coord2)
                val oppDiffVector = Coord(row = -1 * diffVector.row, col = -1 * diffVector.col)

                val validAntiNodeCoords = mutableListOf<Coord>()

                var i = 1
                while(true) {
                    val an1 = coord1 + (diffVector*i)
                    if (an1.row !in 0..grid.maxRow || an1.col !in 0..grid.maxRow) {
                        break
                    }
                    i++
                    validAntiNodeCoords.add(an1)
                }

                var j = 1
                while (true) {
                    val an2 = coord2 + (oppDiffVector*j)
                    if (an2.row !in 0..grid.maxRow || an2.col !in 0..grid.maxRow) {
                        break
                    }
                    j++
                    validAntiNodeCoords.add(an2)
                }

                validAntiNodeCoords
            }
            .flatten()
            .filter { antiNodeCoord ->
                antiNodeCoord.col in 0..grid.maxColumn && antiNodeCoord.row in 0..grid.maxRow
            }.forEach { antiNodeCoord ->
                count++
                grid.updateCell(antiNodeCoord, "#")
            }
    }

//    grid.print()
    grid.filter { it.value != "." }.size.also { println(it) }

    return 0
}


fun <T> computeUniqueCoordPairs(cells: List<Cell<T>>): List<Pair<Coord, Coord>> {
    val coordPairs = mutableListOf<Pair<Coord, Coord>>()

    for (i in 0..cells.size) {
        for (j in i + 1..<cells.size) {
            val a = cells[i]
            val b = cells[j]
            coordPairs.add(Pair(a.coord, b.coord))
        }
    }

    return coordPairs
}

// 1, 2, 3, 4
// 12 || 21
// 13 || 31
// 14 || 41
// 23 || 32
// 24 || 42
// 34 || 43