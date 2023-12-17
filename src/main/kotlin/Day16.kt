
import nico.CardinalFacing
import nico.CardinalFacing.*
import nico.Vec2
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay16_example.txt").readText()
    val linesPart2 = File("inputs/inputDay16_example.txt").readText()
    println("Part1: " + Day16(linesPart1).partOne())
    println("Part2: " + Day16(linesPart2).partTwo())
}

class Day16(input: String) {
    private val contraption = input.lines()
    private val visitedNode = mutableListOf<Vec2>()
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
        )}
        private fun getNextPosition(currentPos: Vec2, direction: CardinalFacing): List<Pair<Vec2, CardinalFacing>>{
            val nextPositions = directionsMap[direction to contraption[currentPos.y][currentPos.x]] ?: listOf(direction)

            return nextPositions
                .map { Pair(currentPos.plus(it), it)}
                .filter {  it.first.x in contraption[0].indices && it.first.y in contraption.indices }
        }
    fun partOne(): Int {
        val beamList = ArrayDeque<Pair<Vec2, CardinalFacing>>()
        beamList.add(Pair(Vec2(0,0), EAST))
        visitedNode.add(Vec2(0,0))

        while(beamList.isNotEmpty()){
            val (currentBeam, dir) = beamList.removeFirst()
            beamList.addAll(getNextPosition(currentBeam, dir))
            visitedNode.addAll(beamList.map { it.first })
        }
        println(visitedNode)
        return visitedNode.size
    }

    fun partTwo(): Int {
        return 0
    }
}