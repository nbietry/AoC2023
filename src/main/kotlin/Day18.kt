import nico.CardinalFacing
import nico.Direction
import nico.Vec2
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay18.txt").readText()
    val linesPart2 = File("inputs/inputDay18_example.txt").readText()
    println("Part1: " + Day18(linesPart1).partOne())
    //println("Part2: " + Day18(linesPart2).partTwo())
}

class Day18(input: String) {

    private val regexInput = Regex("""([A-Z])\s(\d+)\s\(#([0-9a-fA-F]{6})\)""")
    data class Dig(val direction: Direction, val size: Int, val colorHex: String)
    private val digPlan = regexInput.findAll(input).map { matchResult ->
            val (letter, number, colorHex) = matchResult.destructured
            Dig(Direction.valueOf(letter), number.toInt(), colorHex)
        }.toList()
    private val colCount = digPlan.filter { it.direction in setOf(Direction.L, Direction.R) }
        .fold(0 to Int.MIN_VALUE) { (acc, max), line ->
            val newAcc = acc + if (line.direction == Direction.R) line.size else -line.size
            newAcc to maxOf(max, newAcc)
        }

    private val rowCount = digPlan.filter { it.direction in setOf(Direction.U, Direction.D) }
        .fold(0 to Int.MIN_VALUE) { (acc, max), row ->
            val newAcc = acc + if (row.direction == Direction.D) row.size else -row.size
            newAcc to maxOf(max, newAcc)
        }
    private val terrain = List(rowCount.second) { MutableList(colCount.second) { '.' } }

    private fun traceRay(row: Int, col: Int): Int {

        return if(col >= terrain.first().size) 0 // Reached the right side
        else {
            if (terrain[row][col] == '#') 1 + traceRay(row, col + 1)
            else 0 + traceRay(row, col + 1) // Only move east
        }
    }
    fun partOne():String{

        var startNode = Vec2(colCount.first,rowCount.first)
        digPlan.forEach { dig ->
            repeat(dig.size){
                terrain[startNode.y][startNode.x] = '#'
                startNode = startNode.plus(dig.direction.vector)
            }
        }

        terrain.mapIndexed { indexRow: Int, row: MutableList<Char> ->
            List(row.size) { indexCol: Int ->
                if(traceRay(indexRow, indexCol) % 2 > 0) terrain[indexRow][indexCol] = '#'
            }
        }
        terrain.map { println(it) }

        return terrain.sumOf { row ->
            row.count { it == '#' }
        }.toString()

    }

    fun partTwo():String{
        TODO()
    }

}