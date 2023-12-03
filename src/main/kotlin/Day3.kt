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
                return this.numValue.toString() + "=" + adjacentList.toString()
            }
        }
    }

    fun partOne(lines: List<String>): String {
        //Read and store schema
        val engineSchema = lines.map { it.toCharArray() }
        //Extract numbers from schema
        val numberList = lines.flatMap { extractNumbers(it) }
        //Create object list to be analysed
        val schemaNumberList = numberList.map {  SchemaNumber(it.toInt(), listOf<Pos>())}

        var currentNumberCount = 0
        for (i in engineSchema.indices){
            var currentCursor = findNextNumberPosition(0,i, engineSchema)
            while(currentCursor.x < engineSchema[0].size) {
                schemaNumberList[currentNumberCount].adjacentList = getAdjacent(currentCursor, engineSchema)
                currentCursor = findNextNumberPosition(currentCursor.x + schemaNumberList[currentNumberCount].numValue.toString().toCharArray().size ,i, engineSchema)
                currentNumberCount++
            }
        }
        println(schemaNumberList.toString())

        return schemaNumberList.filter { it.isValid(engineSchema) }.sumOf { it.numValue }.toString()
    }

    private fun getAdjacent(currentPosition: Pos, engineSchema: List<CharArray>): List<Pos>{
        var listOfAdjacent = listOf<Pos>()
        //Check north
        val northPos = Pos(currentPosition.x, currentPosition.y - 1)
        if(engineSchema.getOrNull(northPos.y)?.getOrNull(northPos.x) != null )
            if(!engineSchema[northPos.y][northPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + northPos
        //Check south
        val southPos = Pos(currentPosition.x, currentPosition.y + 1)
        if(engineSchema.getOrNull(southPos.y)?.getOrNull(southPos.x) != null )
            if(!engineSchema[southPos.y][southPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + southPos

        //Check east
        val eastPos = Pos(currentPosition.x + 1, currentPosition.y)
        if(engineSchema.getOrNull(eastPos.y)?.getOrNull(eastPos.x) != null )
            listOfAdjacent = if(engineSchema[eastPos.y][eastPos.x].isDigit())
                listOfAdjacent + getAdjacent(eastPos, engineSchema)
            else
                listOfAdjacent + eastPos
        //Check west
        val westPos = Pos(currentPosition.x - 1, currentPosition.y)
        if(engineSchema.getOrNull(westPos.y)?.getOrNull(westPos.x) != null )
            if(!engineSchema[westPos.y][westPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + westPos

        //Check north-east
        val northEastPos = Pos(currentPosition.x + 1, currentPosition.y - 1)
        if(engineSchema.getOrNull(northEastPos.y)?.getOrNull(northEastPos.x) != null )
            if(!engineSchema[northEastPos.y][northEastPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + northEastPos
        //Check north-west
        val northWestPos = Pos(currentPosition.x-1, currentPosition.y - 1)
        if(engineSchema.getOrNull(northWestPos.y)?.getOrNull(northWestPos.x) != null )
            if(!engineSchema[northWestPos.y][northWestPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + northWestPos
        //Check south-east
        val southEastPos = Pos(currentPosition.x+1, currentPosition.y + 1)
        if(engineSchema.getOrNull(southEastPos.y)?.getOrNull(southEastPos.x) != null )
            if(!engineSchema[southEastPos.y][southEastPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + southEastPos
        //Check south-east
        val southWestPos = Pos(currentPosition.x-1, currentPosition.y + 1)
        if(engineSchema.getOrNull(southWestPos.y)?.getOrNull(southWestPos.x) != null )
            if(!engineSchema[southWestPos.y][southWestPos.x].isDigit())
                listOfAdjacent = listOfAdjacent + southWestPos

        return listOfAdjacent
    }
    private fun findNextNumberPosition(x: Int, y: Int, engineSchema: List<CharArray>): Pos {
        var currentX = x
        while (currentX < engineSchema[0].size && !engineSchema[y][currentX].isDigit())
            currentX++
        return Pos(currentX, y)
    }


    fun partTwo(lines: List<String>): String {
        return ""
    }
}