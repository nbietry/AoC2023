import nico.*
import java.io.DataInput
import java.io.File

fun main() {

    val lines = File("inputs/inputDay8.txt").readText()
    val day8 = Day8(lines)
    println("Part1: " + day8.partOne())
    println("Part2: " + day8.partTwo())
}

class Day8(input: String){
    companion object{
        private val networkRegex = """(\w+) = \((\w+), (\w+)\)""".toRegex()
    }
    private val networkNodes = networkRegex.findAll(input).associateBy(
        keySelector = {it.groupValues[1]},
        valueTransform = {it.groupValues[2] to it.groupValues[3]}
    )
    private val instructions = input.split('\n').first()
    fun partOne(): String {
        var counter = 0
        var startNode = "AAA"
        do {
            startNode = instructions.fold(startNode) { acc, instruction ->
                when (instruction) {
                    'L' -> networkNodes[acc]!!.first
                    'R' -> networkNodes[acc]!!.second
                    else -> throw IllegalArgumentException()
                }
            }
            counter++
        }while (startNode != "ZZZ")
        return (counter * instructions.count()).toString()
    }
    fun partTwo(): String {
        return ""
    }
}