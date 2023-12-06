import nico.extractNumbers
import java.io.File

class Day6 {
    fun getDistance(hold: Long, time:Long): Long{
        return (time-hold) * hold
    }
    fun partOne(lines: List<String>): String {
        val times = extractNumbers(lines.first())
        val distances = extractNumbers(lines.last())

        return times.mapIndexed { index, time ->
            (0..<time).map {
                getDistance(it + 1L, time).coerceAtLeast(0)
            }.count { it > distances[index] }
        }.also { println(it) }
            .reduce { acc, i -> acc * i }
            .toString()

    }
    fun partTwo(lines: List<String>): String {
        val times = extractNumbers(lines.first().replace(" ",""))
        val distances = extractNumbers(lines.last().replace(" ",""))

        println(times)
        return times.mapIndexed { index, time ->
            (0L..<time).map {
                getDistance(it + 1L, time).coerceAtLeast(0)
            }.count { it > distances[index] }
        }.also { println(it) }
            .reduce { acc, i -> acc * i }
            .toString()
    }
}


fun main() {
    val day6 = Day6()
    val lines = File("inputs/inputDay6.txt").readLines()

    println("Part1: " + day6.partOne(lines))
    println("Part2: " + day6.partTwo(lines))
}