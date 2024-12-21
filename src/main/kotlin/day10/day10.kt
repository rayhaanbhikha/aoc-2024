package day10

import utils.*

fun score(startingCoord: Coord, grid: Grid<Int>, keepTrack: Boolean = false): Int {
    val startingCellValue = grid.cells.getOrNull(startingCoord.row)?.getOrNull(startingCoord.col) ?: return 0
    if (startingCellValue != 0) return 0

    val trailHeadsSeen = mutableSetOf<Coord>()
    val cellsToSearch = mutableListOf(startingCoord)
    var counter = 0

    while (cellsToSearch.isNotEmpty()) {
        val currentCoord = cellsToSearch.removeFirst()

        val cellValue = grid.cells.getOrNull(currentCoord.row)?.getOrNull(currentCoord.col) ?: continue

        if (cellValue == 9) {
            when {
                keepTrack -> when {
                    !trailHeadsSeen.contains(currentCoord) -> {
                        counter++
                        trailHeadsSeen.add(currentCoord)
                    }
                }
                else -> counter++
            }
            continue
        }

        val expectedNextSearchValue = cellValue + 1

        val neighbours =
            currentCoord.cardinalNeighbours()
                .filter { it.inValidRange(0..grid.maxRow, 0..grid.maxColumn) }
                .mapNotNull { nextCoord ->
                    val nextCellValue =
                        grid.cells.getOrNull(nextCoord.row)?.getOrNull(nextCoord.col) ?: return@mapNotNull null

                    if (expectedNextSearchValue == nextCellValue) {
                        nextCoord
                    } else {
                        null
                    }
                }

        if (neighbours.isNotEmpty()) cellsToSearch.addAll(0, neighbours)
    }


    return counter
}


fun part1(): Int {
//    val input = loadInput("./day10_input_example_3.txt")
//    val input = loadInput("./day10_input_example_2.txt")
    val input = loadInput(10)

    val grid = input.toGrid { num -> if (num == ".") -2 else num.toInt() }

    grid.map {
        score(it.coord, grid, true)
    }.sum().also { println(it) }


    return 0
}

fun part2(): Int {
////    val input = """
////        .....0.
////        ..4321.
////        ..5..2.
////        ..6543.
////        ..7..4.
////        ..8765.
////        ..9....
////    """.trimIndent()
//    val input = """
//..90..9
//...1.98
//...2..7
//6543456
//765.987
//876....
//987....
//    """.trimIndent()
    val input = loadInput(10)

    val grid = input.toGrid { num -> if (num == ".") -2 else num.toInt() }

    grid.map {
        score(it.coord, grid)
    }.sum().also { println(it) }

    return 0
}
