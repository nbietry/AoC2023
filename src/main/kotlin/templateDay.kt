import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDayxx_example.txt").readText()
    val linesPart2 = File("inputs/inputDayxx_example.txt").readText()
    println("Part1: " + templateDay(linesPart1).partOne())
    println("Part2: " + templateDay(linesPart2).partTwo())
}

class templateDay(input: String) {
    fun partOne():String{
        TODO()
    }
    fun partTwo():String{
        TODO()
    }

}