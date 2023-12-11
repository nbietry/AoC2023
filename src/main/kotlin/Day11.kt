import nico.Vec2
import java.io.File
import kotlin.math.abs


fun main() {

    val linesPart1 = File("inputs/inputDay11.txt").readText()
    val linesPart2 = File("inputs/inputDay11.txt").readText()
    println("Part1: " + Day11(linesPart1).partOne())
    println("Part2: " + Day11(linesPart2).partTwo())
}

class Day11(val input: String) {
    data class Universe(val content: List<List<Char>>){
        fun emptyColumns () = (0..<content[0].size).mapNotNull { colIndex ->
            if (content.all { it[colIndex] == '.' }) colIndex else null
        }
        fun emptyRows () = content.mapIndexedNotNull { i, line -> if(line.all {char -> char == '.'}) i else null }
        fun getPairOfGalaxies() = content.mapIndexed{y, row -> row.mapIndexedNotNull { x, col -> if(col =='#') Vec2(x, y) else null } }.flatten()
    }
    private val universe = Universe(input.lines().map { row -> row.map{ col -> col } })
    private lateinit var galaxies : List<Vec2>
    private lateinit var galaxyPairs : List<Pair<Vec2,Vec2>>
    private fun calculateDistance(pos1: Vec2, pos2: Vec2, expandSize: Long): Long {
        val xDiff = pos1.x - pos2.x
        val yDiff = pos1.y - pos2.y
        val getEmptyColsCrossed =
            universe.emptyColumns().count { it in (minOf(pos1.x, pos2.x) + 1)..<maxOf(pos1.x, pos2.x) }
        val getEmptyRowsCrossed =
            universe.emptyRows().count { it in (minOf(pos1.y, pos2.y) + 1)..<maxOf(pos1.y, pos2.y) }

        return abs(xDiff) + getEmptyColsCrossed * (expandSize-1) + abs(yDiff) + getEmptyRowsCrossed * (expandSize-1)
    }
    fun partOne(): String{
        galaxies = universe.getPairOfGalaxies()
        //Get pairs of galaxies with distances
        galaxyPairs = galaxies.flatMapIndexed { index, galaxy ->
            galaxies.subList(index + 1, galaxies.size).map { otherGalaxy ->
                Pair(galaxy, otherGalaxy)
            }
        }
        return galaxyPairs.sumOf { (galaxy1, galaxy2) -> calculateDistance(galaxy1, galaxy2, 2) }.toString()
    }
    fun partTwo(): String{
        if(!::galaxyPairs.isInitialized) partOne()
        return galaxyPairs.sumOf { (galaxy1, galaxy2) -> calculateDistance(galaxy1, galaxy2, 1000000) }.toString()
    }
}
