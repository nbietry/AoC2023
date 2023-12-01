import java.io.File

fun main() {
    val day1 = Day1()
    run part1@{
        val lines = File("inputs/inputDay1.txt").readLines()
        println("Part1: " + day1.partOne(lines))
    }

    run part2@{
        val lines = File("inputs/inputDay1.txt").readLines()
        println("Part2: " + day1.partTwo(lines))
    }
}

class Day1{
    companion object {
        val numbers = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        val regexGetFirstNumber = numbers.map { it.key }.joinToString(prefix = "\\d|", separator = "|").toRegex()
        val regexGetLastNumber = numbers.map { it.key.reversed() }.joinToString(prefix = "\\d|", separator = "|").toRegex()
    }
    fun partOne(lines: List<String>): String {
        val resultPart1 = lines.sumOf { line ->
            line.first { it.isDigit() }.toString().plus(line.last { it.isDigit() }).toInt()
        }
        return resultPart1.toString()
    }
    fun partTwo(lines: List<String>): String {

        return lines.sumOf { line ->
            val firstNumber = regexGetFirstNumber.find(line)!!.value.let { number ->
                number.toIntOrNull() ?: numbers.getValue(number)
            }
            val lastNumber = regexGetLastNumber.find(line.reversed())!!.value.reversed().let { number ->
                number.toIntOrNull() ?: numbers.getValue(number)
            }
            firstNumber.toString().plus(lastNumber.toString()).toInt()
        }.toString()
    }
}