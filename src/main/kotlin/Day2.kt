import java.io.File


fun main() {
    val day2 = Day2()
    run part1@{
        val lines = File("inputs/inputDay2.txt").readLines()
        println("Part1: " + day2.partOne(lines))

        //println("3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green".split(';').map { it.split(',') })
    }

    run part2@{
        val lines = File("inputs/inputDay2.txt").readLines()
        println("Part2: " + day2.partTwo(lines))
    }
}

class Day2 {
    companion object {
        data class Set(val cubeColor: Cube, val quantity: Int){
            fun isValid():Boolean{return quantity > cubeColor.max}
        }
        enum class Cube(val max:Int){RED (12),GREEN(13),BLUE(14);}
        fun parseGame(input: String): List<Set>{
            return input.split(':').last().split(';').flatMap { it.split(',') }.map{
                Set(Cube.valueOf(it.trim().split(' ')[1].uppercase()), it.trim().split(' ')[0].toInt())
            }
        }
    }
    fun partOne(lines: List<String>): String {
        return lines.mapIndexed { index, line ->
            if(parseGame(line).count { set -> set.isValid() } > 0)
                0
            else
                index+1
        }.sum().toString()
    }

    fun partTwo(lines: List<String>): String {
        return lines.sumOf { line ->
            parseGame(line).groupBy(Set::cubeColor).mapValues { entry ->
                entry.value.maxBy { it.quantity }
            }.values.map { it.quantity }.reduce { accumulator, element -> accumulator * element }
        }.toString()
    }


}

