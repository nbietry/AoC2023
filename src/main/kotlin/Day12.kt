import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay12.txt").readText()
    val linesPart2 = File("inputs/inputDay12.txt").readText()
    println("Part1: " + Day12().partOne(linesPart1))
    //println("Part2: " + Day12(linesPart2).partTwo())
}

class Day12() {
    private fun generatePossibilities(input: String): List<String> {
        val result = mutableListOf<String>()

        fun generateHelper(curr: String) {
            if (!curr.contains('?')) {
                result.add(curr)
            }else {
                generateHelper(curr.replaceFirst('?', '.'))
                generateHelper(curr.replaceFirst('?', '#'))
            }
        }
        generateHelper(input)
        return result
    }

    fun countPossibilities(inputChars: String, groups: List<Int>): Int {
        val possibilities = generatePossibilities(inputChars)
        return possibilities.count { isValid(it, groups) }
    }
    private fun isValid(candidate: String, groups: List<Int>): Boolean {
        val regexPattern = buildRegexPattern(groups)
        return regexPattern.matches(candidate)
    }
    private fun buildRegexPattern(groups: List<Int>): Regex {
        val patternBuilder = StringBuilder("^")
        for (group in groups) {
            patternBuilder.append("(\\.{0,1})*(#{$group}(\\.|\$))")
        }
        patternBuilder.append("(\\.{0,1})*$")
        return patternBuilder.toString().toRegex()
    }

    fun partOne(input: String): String {
        val total = input.lines().sumOf { line ->
            val (inputChars, groupSpecification) = line.split(" ")
            val groupList = groupSpecification.split(",").map { it.toInt() }
            //println(" group: $groupList | spring : $inputChars")
            countPossibilities(inputChars, groupList)//.also { println(it) }
        }

        return total.toString()
    }
}