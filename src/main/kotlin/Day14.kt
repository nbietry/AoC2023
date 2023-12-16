
import nico.CardinalFacing
import nico.Vec2
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay14.txt").readText()
    val linesPart2 = File("inputs/inputDay14_example.txt").readText()
    println("Part1: " + Day14(linesPart1).partOne())
    println("Part2: " + Day14(linesPart2).partTwo())
}

class Day14(input: String) {

    private val platform: List<MutableList<Char>> = input.lines().map { it.toCharArray().toMutableList() }
    private tailrec fun tilt(rock: Vec2, direction: CardinalFacing): Vec2 =
        if(rock.plus(direction).y < 0 ||
            rock.plus(direction).y >= platform.size ||
            rock.plus(direction).x < 0 ||
            rock.plus(direction).x >= platform[0].size ||
            platform[rock.plus(direction).y][rock.plus(direction).x] == '#' ||
            platform[rock.plus(direction).y][rock.plus(direction).x] == 'O')
            rock
        else
            tilt(rock.plus(direction), direction)
    private fun moveAllRocks(direction: CardinalFacing) {
        for (y in platform.indices) {
            for (x in platform[y].indices) {
                if (platform[y][x] == 'O') {
                    val nextPosition = tilt(Vec2(x, y), direction)
                    platform[nextPosition.y][nextPosition.x] = 'O'
                    if (Vec2(x, y) != nextPosition) platform[y][x] = '.'
                }
            }
        }
    }

    fun partOne(): String{
        moveAllRocks(CardinalFacing.NORTH)
        return platform.mapIndexed { index, row ->
            row.count { it == 'O' } * (platform.size - index)
        }.sum().toString()
    }



    fun partTwo(): String{
        for(direction in CardinalFacing.entries){
            moveAllRocks(direction)
            println(direction.name + " " + platform.toString())
        }
        return platform.toString()
    }

}