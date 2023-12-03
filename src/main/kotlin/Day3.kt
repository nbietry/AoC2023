import nico.Move
import java.io.File
import nico.extractNumbers
import nico.Pos

fun main() {
    val day3 = Day3()
    run part1@{
        val lines = File("inputs/inputDay3.txt").readLines()
        println("Part1: " + day3.partOne(lines))
    }

    run part2@{
        val lines = File("inputs/inputDay3.txt").readLines()
        println("Part2: " + day3.partTwo(lines))
    }
}
class Day3 {
    companion object{
        data class SchemaNumber(var numValue: Int, var adjacentList: List<Pos>){
            fun isValid(engineSchema: List<CharArray>): Boolean{ return adjacentList.count{engineSchema[it.y][it.x] != '.'} > 0}
            override fun toString(): String {
                return this.numValue.toString()// + "=" + adjacentList.toString()
            }
        }
    }
    private fun getAdjacent(currentPosition: Pos, engineSchema: List<CharArray>): List<Pos> {
        val listOfAdjacent = mutableListOf<Pos>()

        val directions = arrayOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1),  Pair(0, 1),
            Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )

        for ((dx, dy) in directions) {
            val newX = currentPosition.x + dx
            val newY = currentPosition.y + dy

            if (newX in engineSchema[0].indices && newY in engineSchema.indices) {
                // If the right adjacent position is a digit, recursively get its adjacent positions
                if (dx == 1 && dy == 0 && engineSchema[newY][newX].isDigit()) {
                    listOfAdjacent.addAll(getAdjacent(Pos(newX, newY), engineSchema))
                } else {
                    if (!engineSchema[newY][newX].isDigit()) {
                        listOfAdjacent.add(Pos(newX, newY))
                    }
                }
            }
        }
        return listOfAdjacent
    }
    private fun findNextNumberPosition(x: Int, y: Int, engineSchema: List<CharArray>): Pos {
        var currentX = x
        while (currentX < engineSchema[0].size && !engineSchema[y][currentX].isDigit())
            currentX++
        return Pos(currentX, y)
    }
    private fun getStarsPositions(engineSchema: List<CharArray>): List<Pos>{
        val starPosList = mutableListOf<Pos>()
        engineSchema.forEachIndexed{indexRow, line ->
            line.forEachIndexed{indexCol, col ->
                if(col == '*') starPosList.add(Pos(indexCol, indexRow))
            }
        }
        return starPosList
    }
    private fun loadData(lines: List<String>): Pair<List<CharArray>, List<SchemaNumber>> {
        //Read and store schema
        val engineSchema = lines.map { it.toCharArray() }
        //Extract numbers from schema
        val numberList = lines.flatMap { extractNumbers(it) }
        //Create object list to be analysed
        val schemaNumberList = numberList.map { SchemaNumber(it.toInt(), listOf<Pos>()) }

        var currentNumberCount = 0
        for (i in engineSchema.indices) {
            var currentCursor = findNextNumberPosition(0, i, engineSchema)
            while (currentCursor.x < engineSchema[0].size) {
                schemaNumberList[currentNumberCount].adjacentList = getAdjacent(currentCursor, engineSchema)
                currentCursor = findNextNumberPosition(
                    currentCursor.x + schemaNumberList[currentNumberCount].numValue.toString().toCharArray().size,
                    i,
                    engineSchema
                )
                currentNumberCount++
            }
        }
        return Pair(engineSchema, schemaNumberList)
    }

    fun partOne(lines: List<String>): String {
        val (engineSchema, schemaNumberList) = loadData(lines)
        return schemaNumberList.filter { it.isValid(engineSchema) }.sumOf { it.numValue }.toString()
    }


    fun partTwo(lines: List<String>): String {
        //Read and store schema
        val (engineSchema, schemaNumberList) = loadData(lines)
        //Extract positions of all stars
        val starsPosList = getStarsPositions(engineSchema)

        var part2Solution = 0
        for (position in starsPosList) {
            //get numbers having current star position as an adjacent
            val filteredNumbers = schemaNumberList.filter { it.adjacentList.contains(position) }
            //Test if there is exactly 2 if so multiply values and add to result
            if (filteredNumbers.count() == 2) {
                part2Solution += filteredNumbers[0].numValue * filteredNumbers[1].numValue
            }
        }
        return part2Solution.toString()
    }
}