
import nico.CardinalFacing
import nico.Vec2
import java.io.File

fun main() {

    val linesPart1 = File("inputs/inputDay14.txt").readText()
    val linesPart2 = File("inputs/inputDay14.txt").readText()
    println("Part1: " + Day14(linesPart1).partOne())
    println("Part2: " + Day14(linesPart2).partTwo())
}

class Day14(input: String) {

    private var platform: List<MutableList<Char>> = input.lines().map { it.toCharArray().toMutableList() }
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
        val isNorthOrWest = direction == CardinalFacing.NORTH || direction == CardinalFacing.WEST

        val yRange = if (isNorthOrWest) platform.indices else platform.indices.reversed()
        val xRange = if (isNorthOrWest) platform[0].indices else platform[0].indices.reversed()

        yRange.forEach { y ->
            xRange.forEach { x ->
                if (platform[y][x] == 'O') {
                    val nextPosition = tilt(Vec2(x, y), direction)
                    platform[nextPosition.y][nextPosition.x] = 'O'
                    if (Vec2(x, y) != nextPosition) platform[y][x] = '.'
                }
            }
        }

    }
    companion object {
        private const val CYCLES = 1000000000
    }

    fun partOne(): String{
        moveAllRocks(CardinalFacing.NORTH)
        return platform.mapIndexed { index, row ->
            row.count { it == 'O' } * (platform.size - index)
        }.sum().toString()
    }

    fun partTwo(): String{

        var cpt = 0
        val cacheState = mutableMapOf(platform to 0)

        for (i in 1..CYCLES) {
            for (direction in listOf(CardinalFacing.NORTH, CardinalFacing.WEST, CardinalFacing.SOUTH, CardinalFacing.EAST)) {
                moveAllRocks(direction)
            }
            //save current position in a cache
            val j = cacheState.getOrPut(platform.map { it.toMutableList() }) { i }
            //If an existing position is found, calculate the final position and leave the loop
            if (i != j) {
                platform = cacheState.keys.elementAt(j + (CYCLES - i) % (i - j))
                break
            }
        }
        //Do the final calculation
        return platform.mapIndexed { index, row ->
            row.count { it == 'O' } * (platform.size - index)
        }.sum().toString()
    }

}