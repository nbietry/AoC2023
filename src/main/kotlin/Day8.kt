import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay8_part1.txt").readText()
    val linesPart2 = File("inputs/inputDay8_part2.txt").readText()
    println("Part1: " + Day8(linesPart1).partOne())
    println("Part2: " + Day8(linesPart2).partTwo())
}

class Day8(input: String){
    companion object{
        private val networkRegex = """(\w+) = \((\w+), (\w+)\)""".toRegex()
        private fun gcd(a: Long, b: Long): Long {
            return if (b == 0L) a else gcd(b, a % b)
        }

        private fun lcm(a: Long, b: Long): Long {
            return a * b / gcd(a, b)
        }
    }
    private val networkNodes = networkRegex.findAll(input).associateBy(
        keySelector = {it.groupValues[1]},
        valueTransform = {it.groupValues[2] to it.groupValues[3]}
    )
    private val instructions = input.split('\n').first()
    private val getStartingNodes = networkNodes.filter { it.key.endsWith('A') }
    private fun executeInstructions(startNode:String) = instructions.fold(startNode) { acc, instruction ->
        when (instruction) {
            'L' -> networkNodes[acc]!!.first
            'R' -> networkNodes[acc]!!.second
            else -> throw IllegalArgumentException()
        }
    }
    fun partOne(): String {
        //First algo:
        var counter = 0
        var startNode = "AAA"
        do {
            startNode = executeInstructions(startNode)
            counter++
        }while (startNode != "ZZZ")

        //New algo with discovery on Part 2:
        val withPart2AlgoResult = generateSequence("AAA", ::executeInstructions).indexOf("ZZZ") * instructions.count()
        println("Part 1 (with new Algo): $withPart2AlgoResult")

        return (counter * instructions.count()).toString()
    }
    fun partTwo(): String {

        val startNodes = getStartingNodes.map { it.key }
        val result = startNodes.asSequence().map { node ->
            generateSequence(node, ::executeInstructions).withIndex().first { (index, end) -> end.endsWith('Z') }
        }.toList().map { it.index.toLong() }

        //Get the lowest common denominator and multiply by instruction size
        return (result.reduce{acc, i -> lcm(acc, i)} * instructions.count()).toString()

    }
}