import nico.extractNumbers
import java.io.File


fun main() {
    val day5 = Day5()
    run part1@{
        val lines = File("inputs/inputDay5.txt").readText()
        println("Part 1 result is " + day5.partOne(lines))
    }
    run part2@{
        val lines = File("inputs/inputDay5.txt").readText()
        println("Part 2 result is " + day5.partTwoOptimized(lines))

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

    fun processRangeMapping(seed: Long, ranges: List<Range>): Long{
        val matchingRange = ranges.find { seed >= it.start && seed <= it.end }
        return matchingRange?.let { seed - it.diff } ?: seed
    }
    data class AlmanacMap(val name: String, val mappingDic: List<Range>)
    data class Range(val start:Long, val end:Long, val diff:Long){
        override fun toString(): String {
            return ("$start $end $diff")
        }
    }
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
        return seedsList.minOfOrNull { seed ->
            almanacMapList.fold(seed) { acc, almanacMap ->
                processRangeMapping(acc, almanacMap.mappingDic)
            }
        }.toString()
    }
    fun partTwo(input: String): String {
        // Split the input string into individual numbers
        val seeds = extractNumbers(input.split('\n').first())
        // Group the numbers into pairs
        val seedsPair = seeds.chunked(2) { (first, second) -> Pair(first, second) }

        //Load data into structure
        val almanacMapList =
            parseInputData(input).map{block ->
                val mapPairs = block.second.map {
                    Range(it[1], it[1] + it[2], it[1]-it[0])
                }
                AlmanacMap(block.first, mapPairs)
            }

        //Execute find range on the list for each seed number
        return seedsPair.map {
            (it.first..it.first + it.second).toList()
                .minOfOrNull { seed ->
                    almanacMapList.fold(seed) { acc, almanacMap ->
                        processRangeMapping(acc, almanacMap.mappingDic)
                    }
                }
        }.toString()

    }

    fun partTwoOptimized(input: String): String{
        val initialSeedsRanges = input.split('\n')
            .first()
            .split(' ')
            .drop(1)
            .map { it.toLong() }.chunked(2)
            .map { (start, len) -> start..<start + len }

        val layers = input.split("\n\n")
            .drop(1)
            .filter{ it.isNotBlank() }
            .map {
                it.split('\n').mapIndexedNotNull() { index, line ->
                    if (index > 0) {
                        val (dest, source, size) = line.split(' ').map { it.toLong() }
                        Range(dest, dest+size, dest-source)

                    } else null
                }
            }.reversed()

        //Try binary search from location to the feed starting from 0
        var current = 0L
        while (true) {
            current++
            if (initialSeedsRanges.any { range ->
                    val seed = layers.fold(current) { acc, layer ->
                        processRangeMapping(acc, layer)
                    }
                    range.contains(seed)
                }) break
        }

        return current.toString()
    }
}