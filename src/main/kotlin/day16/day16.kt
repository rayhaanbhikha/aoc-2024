package day16

import day6.directions
import utils.*
import java.util.PriorityQueue

class Maze(val grid: Grid<Node>) {
    val start = grid.firstOrNull { it.value.rawValue == "S" } ?: throw Exception("Start not found")
    val end = grid.firstOrNull { it.value.rawValue == "E" } ?: throw Exception("End not found")

    fun shortestPath() {
        val priorityQueue = PriorityQueue<Node>(compareBy { it.weight })
        priorityQueue.add(start.value)

        while (priorityQueue.isNotEmpty()) {
            val currentNode = priorityQueue.poll()

            // Stop if we reach the end node
            if (currentNode.rawValue == "E") {
                println("Reached the end with weight: ${currentNode.weight}")
                return
            }

            // Process neighbors
            currentNode.coord.cardinalNeighboursWithDirections()
                .mapNotNull { (direction, coord) ->
                    val neighbor = grid.get(coord) ?: return@mapNotNull null

                    // Ignore walls
                    if (neighbor.isWall) return@mapNotNull null

                    // Calculate new weight
                    val directionChangePenalty = if (currentNode.directions != null &&
                        currentNode.directions != direction) 1000 else 0
                    val newWeight = currentNode.weight + 1 + directionChangePenalty

                    // Relaxation: Update weight if a shorter path is found
                    if (newWeight < neighbor.weight) {
                        neighbor.weight = newWeight
                        neighbor.directions = direction
                        return@mapNotNull neighbor
                    }

                    null
                }
                .forEach { neighbor ->
                    // Add updated neighbors to the priority queue
                    if (!priorityQueue.contains(neighbor)) {
                        priorityQueue.add(neighbor)
                    }
                }
        }
    }

    fun walk() {
        val nodesToVisit = mutableListOf(end)
        val nodesVisited = mutableSetOf<Node>()

        while (nodesToVisit.isNotEmpty()) {
            val currentNode = nodesToVisit.removeFirst()

            currentNode.value.wasUsed = true

            nodesVisited.add(currentNode.value)

            if (currentNode.value.rawValue == "S") {
                continue
            }

            val neighbourNodes = currentNode
                .cardinalNeighbours(grid)
                .sortedBy { it.value.weight }
                .filter { !nodesVisited.contains(it.value) }



            if (neighbourNodes.isNotEmpty()) {
                nodesToVisit.add(neighbourNodes[0])
            }
        }
    }

    fun print() {
        for (i in 0..grid.maxRow) {
            for (j in 0..grid.maxColumn) {
                val cell = grid.cells[i][j]
                val r = when {
                    cell.isWall -> "${Colors.Beige.ansiValue}#${Colors.Reset.ansiValue}"
                    cell.rawValue == "S" || cell.rawValue == "E" -> "${Colors.Blue.ansiValue}${cell.rawValue}${Colors.Reset.ansiValue}"
                    cell.directions != null -> when {
                        cell.wasUsed -> "${Colors.Green.ansiValue}${cell.directions!!.getChar()}${Colors.Reset.ansiValue}"
                        else -> "."
                    }
                    // prints S or E
                    else -> cell.rawValue
                }
                print(r)
            }
            println()
        }
        println()
    }
}

data class Node(
    val coord: Coord,
    var weight: Long,
    val isWall: Boolean,
    val rawValue: String,
    var directions: Directions?,
    var wasUsed: Boolean = false
)

fun part1(): Long {
    val input = loadInput(16)
//    val input = loadInput("./day16_input_example2.txt")
    val grid = input.toGrid { it, coord ->
        Node(
            coord,
            if (it == "S") 0 else Long.MAX_VALUE,
            it == "#",
            it,
            if (it == "S") Directions.East else null
        )
    }

    val maze = Maze(grid)

    maze.shortestPath()
    maze.walk()

//    maze.print()

    return maze.end.value.weight.also { println(it) }
}

fun part2(): Int {
    return 0
}
