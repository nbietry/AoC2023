import nico.extractNumbers
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
    private fun parseInputData(input: String): List<Pair<String, List<List<Long>>>> {
        return input.split("\n\n")
            .drop(1)
            .map { it ->
                val (name, list) = it.split(':')
                Pair(name, list.split('\n').drop(1).map { extractNumbers(it) })
            }
    }
    fun buildRangeMap(toNum: Long, fromNum: Long, rangeSize: Long): List<Pair<Long, Long>>{
        return (0..<rangeSize).mapIndexed { index, _ ->
            Pair(fromNum + index, toNum + index)
        }
    }
    fun findRange(seed: Long, ranges: List<Range>): Long{
        val matchingRange = ranges.find { seed >= it.start && seed <= it.end }
        return matchingRange?.let { seed - it.diff } ?: seed
    }
    data class AlmanacMap(val name: String, val mappingDic: List<Range>)
    data class Range(val start:Long, val end:Long, val diff:Long)
    fun partOne(input: String): String {
        val seedsList = extractNumbers(input.split('\n').first())

        //Load data into structure
        val almanacMapList =
            parseInputData(input).map{block ->
                val mapPairs = block.second.map {
                    Range(it[1], it[1] + it[2], it[1]-it[0])
                }
                AlmanacMap(block.first, mapPairs)
            }

        //Execute find range on the list for each seed number

        return seedsList.minOf{
            var initialValue = it
            almanacMapList.mapIndexed{index, almanacMap ->
                initialValue = findRange(initialValue, almanacMapList[index].mappingDic)
                initialValue
            }.last()
        }.toString()
    }

    fun partTwo(lines: List<String>): String {
        return ""
    }
}