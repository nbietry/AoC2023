import nico.extractIntNumbers
import org.w3c.dom.ranges.Range
import java.io.File

fun main() {
    val day5 = Day5()
    run part1@{
        val lines = File("inputs/inputDay5.txt").readText()
        println(day5.partOne(lines))

    }
    run part2@{
        val lines = File("inputs/inputDay5.txt").readText()
    }
}

class Day5 {
    private fun parseInputData(input: String): List<Pair<String, List<List<Int>>>> {
        return input.split("\n\n")
            .drop(1)
            .map { it ->
                val (name, list) = it.split(':')
                Pair(name, list.split('\n').drop(1).map { extractIntNumbers(it) })
            }
    }

    fun buildRangeMap(toNum: Int, fromNum: Int, rangeSize: Int): List<Pair<Int, Int>>{
        return (0..<rangeSize).mapIndexed { index, _ ->
            Pair(fromNum + index, toNum + index)
        }
    }
    data class AlmanacMap(val name: String, val mappingDic: List<Pair<Int, Int>>)
    fun partOne(input: String): String {
        val seedsList = extractIntNumbers(input.split('\n').first())

        //Load data into structure
        val almanacMapList = mutableListOf<AlmanacMap>()
        for (almanacMap in parseInputData(input)){
            almanacMapList.addAll(almanacMap.second.map {
                AlmanacMap(almanacMap.first, buildRangeMap(it[0], it[1], it[2]))
            })
        }

        //Execute find range on the list for each seed number


        return almanacMapList.toString()
    }

    fun partTwo(lines: List<String>): String {
        return ""
    }
}