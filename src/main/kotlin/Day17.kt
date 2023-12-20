
import nico.Vec2
import java.io.File
import java.util.*

fun main() {

    val linesPart1 = File("inputs/inputDay17_example.txt").readText()
    val linesPart2 = File("inputs/inputDay17_example.txt").readText()
    println("Part1: " + Day17(linesPart1).partOne())
    //println("Part2: " + Day17(linesPart2).partTwo())
}

class Day17(input: String) {
    val map = input.lines()
    data class Node(val x: Int, val y: Int, val cost: Int, val moves: Int, val prevDir: Pair<Int, Int>)

    private fun dijkstra(map: List<String>, start: Node): Array<IntArray> {
        val numRows = map.size
        val numCols = map[0].length

        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        val minHeap = PriorityQueue<Node>(compareBy { it.cost })
        val visited = Array(numRows) { BooleanArray(numCols) }
        val cost = Array(numRows) { IntArray(numCols){ Int.MAX_VALUE} }
        visited[0][0] = true
        cost[0][0] = 0
        minHeap.offer(start)

        while (minHeap.isNotEmpty()) {
            val node = minHeap.poll() ?: return cost

            for ((dy, dx) in directions) {
                val newX = node.x + dx
                val newY = node.y + dy
                if (newX !in map[0].indices || newY !in map.indices) continue
                if (cost[newY][newX] < (node.cost + map[newY][newX].digitToInt())) continue

                if (node.moves < 4) {
                    val newCost = node.cost + Character.getNumericValue(map[newY][newX])
                    cost[newY][newX] = newCost
                    val newMoves = if ((node == start)||(dx == node.prevDir.first && dy == node.prevDir.second)) node.moves + 1 else 1
                    minHeap.offer(Node(newX, newY, newCost, newMoves, dx to dy))
                }
            }
        }

        return cost// If no path found
    }
    fun partOne():String{
        val startPosition = Node(0, 0, 0, 0, 0 to 0)
        val endPosition = Vec2(map[0].length-1, map.size-1)

        return dijkstra(map, startPosition).also { it.map { println(it.toList().map { it.toString().padStart(3, ' ') }) } }[endPosition.y][endPosition.x].toString()

    }
    fun partTwo():String{
        TODO()
    }

}