
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nico.CardinalFacing
import nico.CardinalFacing.*
import nico.Vec2
import java.io.File

fun main() {
    val linesPart1 = File("inputs/inputDay16.txt").readText()
    val linesPart2 = File("inputs/inputDay16.txt").readText()
    println("Part1: " + Day16(linesPart1).partOne())
    runBlocking {
        println("Part2: " + Day16(linesPart2).partTwo())
    }
}

class Day16(input: String) {
    private val contraption = input.lines()

    data class Beam(val position: Vec2, val direction: CardinalFacing)
    companion object {
        private val directionsMap = mapOf(
            NORTH to '/' to listOf(EAST),
            NORTH to '\\' to listOf(WEST),
            NORTH to '-' to listOf(WEST, EAST),
            WEST to '/' to listOf(SOUTH),
            WEST to '|' to listOf(NORTH, SOUTH),
            WEST to '\\' to listOf(NORTH),
            SOUTH to '/' to listOf(WEST),
            SOUTH to '-' to listOf(WEST, EAST),
            SOUTH to '\\' to listOf(EAST),
            EAST to '/' to listOf(NORTH),
            EAST to '|' to listOf(NORTH, SOUTH),
            EAST to '\\' to listOf(SOUTH)
        )
        fun renderContraptionWithVisited(contraption: List<String>, visitedNodes: List<Vec2>) {
            for ((y, row) in contraption.withIndex()) {
                val newRow = StringBuilder(row)
                for (node in visitedNodes) {
                    if (node.y == y && node.x in row.indices) {
                        newRow[node.x] = '#' // Symbol representing visited nodes
                    }
                }
                println(newRow)
            }
        }
    }
    private fun getNextPosition(beam: Beam): List<Beam> {
        val nextPositions =
            directionsMap[beam.direction to contraption[beam.position.y][beam.position.x]] ?: listOf(beam.direction)

        return nextPositions.map { Beam(beam.position.plus(it), it) }
            .filter { it.position.x in contraption[0].indices && it.position.y in contraption.indices }
    }
    private fun countEnergized(startBeam: Beam): Int {
        val beamList = ArrayDeque<Beam>()
        val visitedNode = mutableListOf<Beam>()

        beamList.add(startBeam)
        visitedNode.add(startBeam)

        while (beamList.isNotEmpty()) {
            val currentBeam = beamList.removeLast()
            for (nextBeam in getNextPosition(currentBeam)) {
                if (nextBeam !in visitedNode) {
                    beamList.add(nextBeam)
                    visitedNode.add(nextBeam)
                }
            }
        }
        //renderContraptionWithVisited(contraption, visitedNode.map { it.position })
        return visitedNode.map { it.position }.toSet().count()
    }
    fun partOne(): Int {
        val startBeam = Beam(Vec2(0, 0), EAST)
        return countEnergized(startBeam)
    }
    suspend fun partTwo(): Int = channelFlow {

        val topSide = (0..<contraption[0].length).map { Vec2(it, 0) }.map { Beam(it, SOUTH) }
        val bottomSide = (0..<contraption[0].length).map { Vec2(it, contraption.size - 1) }.map { Beam(it, NORTH) }
        val leftSide = contraption.indices.map { Vec2(0, it) }.map { Beam(it, EAST) }
        val rightSide = contraption.indices.map { Vec2(contraption[0].length - 1, it) }.map { Beam(it, WEST) }

        for(start in topSide)
            launch { send(countEnergized(start)) }
        for(start in bottomSide)
            launch { send(countEnergized(start)) }
        for (start in leftSide)
            launch { send(countEnergized(start)) }
        for (start in rightSide)
            launch { send(countEnergized(start)) }

    }.reduce(::maxOf)

}