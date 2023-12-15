import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay13.txt").readText()
    val linesPart2 = File("inputs/inputDay13.txt").readText()
    println("Part1: " + Day13(linesPart1).partOne())
    println("Part2: " + Day13(linesPart2).partTwo())
}

class Day13(input: String) {
    private val groups: List<List<String>> = input.split("\n\n").map { it.split('\n') }
    private fun Iterable<String>.transpose(): List<String> = buildList {
        val strings = this@transpose.filterTo(mutableListOf()) { it.isNotEmpty() }
        var i = 0
        while (strings.isNotEmpty()) {
            add(buildString(strings.size) { for (string in strings) append(string[i]) })
            i++
            strings.removeAll { it.length == i }
        }
    }
    private fun findSymmetry(singleGroup: List<String>): Int{
        for(i in 1.. singleGroup.first().lastIndex){
            val firstPart = singleGroup[0].substring(0, i)
            val secondPart = singleGroup[0].substring(i, singleGroup[0].length)
            val n = minOf(firstPart.length, secondPart.length)
            if(colEquals(singleGroup, i, n))
                return i
        }
        return 0
    }
    private fun findSymmetryPart2(singleGroup: List<String>): Int{
        for(i in 1.. singleGroup.first().lastIndex){
            val firstPart = singleGroup[0].substring(0, i)
            val secondPart = singleGroup[0].substring(i, singleGroup[0].length)
            val n = minOf(firstPart.length, secondPart.length)
            if(colAlmostEquals(singleGroup, i, n))
                return i
        }
        return 0
    }
    private fun colEquals(singleGroup: List<String>, i: Int, n: Int) =
        singleGroup.all { line ->
            line.substring(0, i).takeLast(n).reversed() == line.substring(
                i,
                singleGroup[0].length
            ).substring(0, n)
        }
    private fun colAlmostEquals(singleGroup: List<String>, i: Int, n: Int): Boolean {
        val totalDiff = singleGroup.sumOf { line ->
            var diffCount = 0
            val firstSubstring = line.substring(0, i).takeLast(n).reversed()
            val secondSubstring = line.substring(i, singleGroup[0].length).substring(0, n)

            for (index in firstSubstring.indices) {
                if (firstSubstring[index] != secondSubstring[index]) {
                    diffCount++
                }
            }
            diffCount
        }
        return totalDiff == 1
    }

    fun partOne(): String {
        val total = groups.sumOf {  findSymmetry(it) + 100 * findSymmetry(it.transpose())}
        return total.toString()
    }

    fun partTwo(): String {
        val total = groups.sumOf { group ->
            100 * findSymmetryPart2(group.transpose()) +  findSymmetryPart2(group)
        }
        return total.toString()
    }

}