
import nico.Direction
import nico.Vec2
import nico.perimeter
import nico.shoelaceArea
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay18.txt").readText()
    val linesPart2 = File("inputs/inputDay18.txt").readText()
    println("Part1: " + Day18(linesPart1).partOne())
    println("Part2: " + Day18(linesPart2).partTwo())
}

class Day18(input: String) {

    private val regexInput = Regex("""([A-Z])\s(\d+)\s\(#([0-9a-fA-F]{6})\)""")
    data class Dig(val direction: Direction, val size: Int, val colorHex: String)
    private val digPlan = regexInput.findAll(input).map { matchResult ->
            val (letter, number, colorHex) = matchResult.destructured
            Dig(Direction.valueOf(letter), number.toInt(), colorHex.removeSurrounding("(#", ")"))
        }.toList()
    private fun retrieveVertices(directions: List<Pair<Direction, Int>>): List<Vec2> {
        return directions.scan(Vec2(0,0)){ acc, dir ->
            Vec2(acc.x + dir.first.vector.x * dir.second, acc.y + dir.first.vector.y * dir.second)
        }
    }

    fun partOne():String{
        val vectorList = digPlan.map { Vec2(it.direction.vector.x * it.size, it.direction.vector.y * it.size) }
        val vertices = retrieveVertices(digPlan.map { Pair(it.direction, it.size) })
        return (shoelaceArea(vertices) + perimeter(vectorList) / 2 + 1).toString()
    }

    fun partTwo():String{
        val newDigPlan = digPlan.map { Pair(Direction.entries[it.colorHex.last().digitToInt()], it.colorHex.substring(0,5).toInt(radix = 16))}
        val vectorList = newDigPlan.map { Vec2(it.first.vector.x * it.second, it.first.vector.y * it.second) }
        val vertices = retrieveVertices( newDigPlan )
        return (shoelaceArea(vertices) + perimeter(vectorList) / 2L + 1L).toString()
    }

}