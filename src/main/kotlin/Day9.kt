import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay9.txt").readText()
    val linesPart2 = File("inputs/inputDay9_example.txt").readText()
    println("Part1: " + Day9(linesPart1).partOne())
    //println("Part2: " + Day9(linesPart2).partTwo())
}

class Day9(val input: String) {

    fun computeLine(numbers: List<Long>): Long {
        return  generateSequence(numbers) { currentLine ->
            val nextLine = currentLine.zipWithNext { a, b -> b - a }
            nextLine.takeIf { it.any { it != 0L } }//.also { println(it) }
        }.toList().reversed().fold(0L){ acc, line ->
            acc + line.last()
        }
    }
    fun partOne(): String {
        val entries = input.split('\n').map { it.split(' ').map { it.toLong() } }
        return entries.sumOf { computeLine(it) }.toString()
    }

    fun partTwo(): String {
        TODO()
    }

}