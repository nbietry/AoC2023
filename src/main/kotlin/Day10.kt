import nico.Pos
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay10_example.txt").readText()
    val linesPart2 = File("inputs/inputDay10.txt").readText()
    println("Part1: " + Day10(linesPart1).partOne())
    println("Part2: " + Day10(linesPart2).partTwo())
}

class Day10(val input: String) {
    data class Maze(val content: List<MutableList<Char>>, val height: Int, val width: Int) {
        val distances = List(height) { MutableList(width) { -1 } }
        private val possibleDirections = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        fun findStart(): Pos {
            return this.content.flatMapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { colIndex, char ->
                    if (char == 'S') Pos(colIndex, rowIndex) else null
                }
            }.first() // 'S' character always exists, so returning the first occurrence
        }
        fun renderMaze() {
            for (i in content.indices) {
                for (j in content[i].indices) {
                    print(when (content[i][j]) {
                        '.' -> " " // Empty space
                        'F' -> "┏" // Corner for 'F'
                        'L' -> "┗" // Corner for 'L'
                        'J' -> "┛" // Corner for 'J'
                        '7' -> "┓" // Same corner shape as 'J' for '7'
                        'S' -> "S" // Start point
                        '|' -> "┃"
                        '-' -> "━"
                        else -> content[i][j]
                    })
                }
                println() // Move to the next line after each row
            }
        }
        override fun toString(): String {
            return content.joinToString("\n") {
                it.joinToString(" ") {
                    it.toString()
                        .padStart(2, ' ')
                }
            }
        }
        fun getNextPossiblePositions(currentPosition: Pos): List<Pos> {
            val nextPositions = ArrayDeque<Pos>()
            for ((dx, dy) in possibleDirections) {
                if (currentPosition.y + dy >= height || currentPosition.x + dx >= width ||
                    currentPosition.y + dy < 0 || currentPosition.x + dx < 0
                )
                    continue
                when (content[currentPosition.y + dy][currentPosition.x + dx]) {
                    '|' -> {
                        if (dx == 0)
                            nextPositions.add(Pos(currentPosition.x, currentPosition.y + dy))
                    }

                    '-' -> {
                        if (dy == 0)
                            nextPositions.add(Pos(currentPosition.x + dx, currentPosition.y))
                    }

                    'L' -> {
                        if ((dy == 1 && dx == 0) || (dy == 0 && dx == -1))
                            nextPositions.add(Pos(currentPosition.x + dx, currentPosition.y + dy))
                    }

                    'J' -> {
                        if ((dy == 0 && dx == 1) || (dy == 1 && dx == 0))
                            nextPositions.add(Pos(currentPosition.x + dx, currentPosition.y + dy))
                    }

                    '7' -> {
                        if ((dy == 0 && dx == 1) || (dy == -1 && dx == 0))
                            nextPositions.add(Pos(currentPosition.x + dx, currentPosition.y + dy))
                    }

                    'F' -> {
                        if ((dy == -1 && dx == 0) || (dy == 0 && dx == -1))
                            nextPositions.add(Pos(currentPosition.x + dx, currentPosition.y + dy))
                    }

                    'S' -> {
                        nextPositions.add(Pos(currentPosition.x + dx, currentPosition.y + dy))
                    }

                    else -> continue
                }
            }
            return nextPositions.toList()
        }
        fun mazeBFS(start: Pos): List<Pos> {
            val parentMap = mutableMapOf<Pos, Pos>()
            val nodeToCheck = ArrayDeque<IndexedValue<Pos>>()
            val visitedNode = ArrayDeque<Pos>()

            //Add start nodes to the list
            visitedNode.add(start)
            parentMap[getNextPossiblePositions(start).first()] = start
            nodeToCheck.add(IndexedValue(0, getNextPossiblePositions(start).first()))
            while (nodeToCheck.isNotEmpty()) {
                val (index, currentNode) = nodeToCheck.removeFirst()

                if (!visitedNode.contains(currentNode)) {
                    visitedNode.add(currentNode)
                    //distances[currentNode.y][currentNode.x] = 0
                }
                for (nextPosition in this.getNextPossiblePositions(currentNode)) {
                    if(nextPosition !in visitedNode) {
                        parentMap[nextPosition] = currentNode
                        nodeToCheck.add(IndexedValue(index + 1,nextPosition))
                    }
                }
            }


            // Reconstruct the shortest path
            val shortestPath = mutableListOf<Pos>()
            var currentPos = visitedNode.last()
            while (currentPos != start) {
                shortestPath.add(currentPos)
                currentPos = parentMap[currentPos]!!
            }
            shortestPath.add(start)

            return shortestPath.reversed() // Return the shortest path



        }
    }

    private val maze =
        Maze(input.lines().map { it.toCharArray().toMutableList() }, input.lines().size, input.lines().first().length)
    private lateinit var startPosition: Pos
    private lateinit var loopNodeList : List<Pos>
    private fun isWall(char: Char): Boolean {
        return char == '|' || char == 'J'|| char == 'L' || char == 'S'
    }

    private fun traceRay(row: Int, col: Int): Int {

        return if(col >= maze.width) 0 // Reached the right side
        else {
            //print(" $col, $row: " + maze.content[row][col])
            if (isWall(maze.content[row][col])) 1 + traceRay(row, col + 1)
            else 0 + traceRay(row, col + 1) // Only move east
        }
    }

    fun partOne(): String {
        val nodeToCheck = ArrayDeque<IndexedValue<Pos>>()
        val visitedNode = ArrayDeque<Pos>()
        startPosition = maze.findStart()
        visitedNode.add(startPosition)
        //maze.distances[startPosition.y][startPosition.x] = 0
        nodeToCheck.addAll(maze.getNextPossiblePositions(startPosition).map { IndexedValue(1, it) })
        var counter = 0
        while (nodeToCheck.isNotEmpty()) {
            val (index, currentNode) = nodeToCheck.removeFirst()
            if (!visitedNode.contains(currentNode)) {
                visitedNode.add(currentNode)
                //maze.distances[currentNode.y][currentNode.x] = index
            } else continue
            nodeToCheck.addAll(maze.getNextPossiblePositions(currentNode).filter { !visitedNode.contains(it) }
                .map { IndexedValue(index + 1, it) })
            counter = index
        }

        loopNodeList = visitedNode
        return counter.toString()
    }

    fun partTwo(): String {
        //Start part one to initialize distance within Maze
        if (!::startPosition.isInitialized) partOne()

        //Calculate the main loop
        val initialLoop = maze.mazeBFS(startPosition)

        //Clean the maze to keep only the main loop
        val toBeRayTraced = ArrayDeque<Pos>()
        for (y in maze.content.indices) {
            for (x in maze.content[y].indices) {
                val currentPos = Pos(x, y)
                val isInInitialLoop = initialLoop.any { it == currentPos }

                if (!isInInitialLoop) {
                    maze.content[y][x] = '?'
                    toBeRayTraced.add(Pos(x,y))
                }
            }
        }

        //Check every point out of the maze main loop with Ray Tracing
        val finalList = ArrayDeque<Pos>()
        for (node in toBeRayTraced) {
            if (traceRay(node.y, node.x) % 2 > 0) {
                maze.content[node.y][node.x] = 'I'
                finalList.add(Pos(node.x, node.y))
            } else {
                maze.content[node.y][node.x] = 'O'

            }
        }
        maze.renderMaze()

        return finalList.count().toString()
    }
}