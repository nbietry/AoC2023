import nico.CardinalFacing
import nico.CardinalFacing.*
import nico.Vec2
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay16.txt").readText()
    val linesPart2 = File("inputs/inputDay16_example.txt").readText()
    println("Part1: " + Day16(linesPart1).partOne())
    println("Part2: " + Day16(linesPart2).partTwo())
}

class Day16(input: String) {
    private val contraption = input.lines()
    private val visitedNode = mutableListOf<Beam>()

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

    fun partOne(): Int {
        val beamList = ArrayDeque<Beam>()
        val startBeam = Beam(Vec2(0, 0), EAST)
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

    fun partTwo(): Int {
        return 0
    }
}