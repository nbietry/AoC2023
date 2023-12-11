import nico.Pos
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay10_example.txt").readText()
    val linesPart2 = File("inputs/inputDay10_example.txt").readText()
    //println("Part1: " + Day10(linesPart1).partOne())
    println("Part2: " + Day10(linesPart2).partTwo())
}

class Day10(val input: String) {
    data class Maze(val content: List<List<Char>>, val height: Int, val width: Int) {
        val distances = List(height) { MutableList(width) { -1 } }
        private val possibleDirections = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        fun findStart(): Pos {
            return this.content.flatMapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { colIndex, char ->
                    if (char == 'S') Pos(colIndex, rowIndex) else null
                }
            }.first() // 'S' character always exists, so returning the first occurrence
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

        fun exploreOpenMaze(listNodeToExplore: List<Pos>) {
            val nodeToCheck = ArrayDeque<IndexedValue<Pos>>()
            val visitedNode = ArrayDeque<Pos>()
            nodeToCheck.addAll(listNodeToExplore.map{IndexedValue(0, it)})
            while (nodeToCheck.isNotEmpty()) {
                val (index, currentNode) = nodeToCheck.removeFirst()
                if (!visitedNode.contains(currentNode)) {
                    visitedNode.add(currentNode)
                    distances[currentNode.y][currentNode.x] = 0
                } else continue

                for ((dx, dy) in possibleDirections) {
                    if (currentNode.y + dy >= height || currentNode.x + dx >= width ||
                        currentNode.y + dy < 0 || currentNode.x + dx < 0
                    ) continue

                    if (content[currentNode.y + dy][currentNode.x + dx] == '.')
                        nodeToCheck.add(IndexedValue(index + 1,Pos(currentNode.x+dx, currentNode.y + dy)))
                    else continue
                }
            }

        }
    }

    private val maze =
        Maze(input.lines().map { it.toCharArray().toList() }, input.lines().size, input.lines().first().length)
    private lateinit var startPosition: Pos
    fun partOne(): String {
        val nodeToCheck = ArrayDeque<IndexedValue<Pos>>()
        val visitedNode = ArrayDeque<Pos>()
        startPosition = maze.findStart()
        visitedNode.add(startPosition)
        maze.distances[startPosition.y][startPosition.x] = 0
        nodeToCheck.addAll(maze.getNextPossiblePositions(startPosition).map { IndexedValue(1, it) })
        var counter = 0
        while (nodeToCheck.isNotEmpty()) {
            val (index, currentNode) = nodeToCheck.removeFirst()
            if (!visitedNode.contains(currentNode)) {
                visitedNode.add(currentNode)
                maze.distances[currentNode.y][currentNode.x] = index
            } else continue
            nodeToCheck.addAll(maze.getNextPossiblePositions(currentNode).filter { !visitedNode.contains(it) }
                .map { IndexedValue(index + 1, it) })
            counter = index
        }
        println(maze.toString())
        println()

        return counter.toString()
    }

    fun partTwo(): String {
        //Start part one to initialize distance within Maze
        if (!::startPosition.isInitialized) partOne()

        //Get all -1 values (not par of the path) and check if there is a path to Start same from border
        maze.exploreOpenMaze(maze.content.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, char ->
                if (char == '.' && (colIndex== 0 || rowIndex == 0 || colIndex == maze.width-1 || rowIndex == maze.height-1))
                    Pos(colIndex, rowIndex) else null
            }
        })

        println(maze.distances.joinToString("\n") {
            it.joinToString(" ") {
                it.toString()
                    .padStart(2, ' ')
            }
        }
        )
        val listNodeToCheck = maze.distances.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, char ->
                if (char == -1) Pos(colIndex, rowIndex) else null
            }
        }

        return listNodeToCheck.count().toString()
    }
}