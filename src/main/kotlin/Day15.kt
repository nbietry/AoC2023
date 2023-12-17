import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay15.txt").readText()
    val linesPart2 = File("inputs/inputDay15_example.txt").readText()
    println("Part1: " + Day15(linesPart1).partOne())
    println("Part2: " + Day15(linesPart2).partTwo())
}

class Day15(input: String) {

    private var sequence = input.split(',')
    fun partOne(): Int = sequence.sumOf { it.hash() }
    fun partTwo(): Int {
        return 0
    }

    companion object{
        private fun String.hash():Int = fold(0){acc, char -> ((acc + char.code) * 17) % 256}
    }
}


