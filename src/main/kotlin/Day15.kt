import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay15.txt").readText()
    val linesPart2 = File("inputs/inputDay15.txt").readText()
    println("Part1: " + Day15(linesPart1).partOne())
    println("Part2: " + Day15(linesPart2).partTwo())
}

class Day15(input: String) {

    private var sequence = input.split(',')
    fun partOne(): Int = sequence.sumOf { it.hash() }
    fun partTwo(): Int {
        val facility = List(256) { mutableMapOf<String, Int>() }

        for(step in sequence){
            val (label, focalLength) = step.split('-','=')
            if(step.indexOf('=') > 0)
                facility[label.hash()][label] = focalLength.toInt()
            else
                facility[label.hash()].remove(label)
        }

        return facility.withIndex().sumOf { (index, box) ->
            (index + 1) * box.values.withIndex().sumOf { (slot, focalLength) -> (slot + 1) * focalLength }
        }
    }

    companion object{
        private fun String.hash():Int = fold(0){acc, char -> ((acc + char.code) * 17) % 256}
    }
}


