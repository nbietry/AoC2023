package nico
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

fun extractNumbers(input: String): List<Long> {
    val p: Pattern = Pattern.compile("\\d+")
    val m: Matcher = p.matcher(input)
    val output: MutableList<Long> = ArrayList()
    while (m.find()) {
        output.add(m.group().toLong())
    }
    return output
}
fun extractIntNumbers(input: String): List<Int> {
    val p: Pattern = Pattern.compile("\\d+")
    val m: Matcher = p.matcher(input)
    val output: MutableList<Int> = ArrayList()
    while (m.find()) {
        output.add(m.group().toInt())
    }
    return output
}
fun extractNumbersWithSign(input: String): List<Long> {
    val p: Pattern = Pattern.compile("-?\\d+")
    val m: Matcher = p.matcher(input)
    val output: MutableList<Long> = ArrayList()
    while (m.find()) {
        output.add(m.group().toLong())
    }
    return output
}

class Graph {
    private data class Vertex(val name: String) {
        val neighbors = mutableSetOf<Vertex>()
    }
    private val vertices = mutableMapOf<String, Vertex>()
    private operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()
    fun addVertex(name: String) {
        if(vertices[name] == null) vertices[name] = Vertex(name)
    }
    private fun connect(first: Vertex, second: Vertex) {
        first.neighbors.add(second)
        second.neighbors.add(first)
    }
    fun connect(first: String, second: String) = connect(this[first], this[second])
    fun neighbors(name: String) = vertices[name]?.neighbors?.map { it.name } ?: listOf()
    fun dfs(start: String, finish: String): Int = dfs(this[start], this[finish], setOf()) ?: -1
    private fun dfs(start: Vertex, finish: Vertex, visited: Set<Vertex>): Int? =
        if (start == finish)  0
        else {
            val min = start.neighbors
                .filter { it !in visited }
                .mapNotNull { dfs(it, finish, visited + start) }
                .minOrNull()
            if (min == null) null else min + 1
        }
    override fun toString(): String {
        return vertices.toString()
    }
}
fun findPathAStar(size: Vec2,cost: (Vec2) -> Double,heuristic: (Vec2, Vec2) -> Double,neighbors: (Vec2) -> List<Vec2>,start: Vec2,end: Vec2): List<Vec2>? {

    val costs = matrix2dOf(size.x, size.y) { _, _ -> Double.MAX_VALUE }
    val parent = matrix2dOf<Vec2?>(size.x, size.y) { _, _ -> null }
    val fScore = matrix2dOf(size.x, size.y) { _, _ -> Double.MAX_VALUE }

    val openSet = PriorityQueue<Vec2> { one, two -> fScore[one].compareTo(fScore[two]) }
    val closeSet = HashSet<Vec2>()

    openSet.add(start)
    costs[start] = 0.0
    fScore[start] = heuristic(start, end)

    while (openSet.isNotEmpty()) {

        // Grab the next node with the lowest cost
        val cheapestNode: Vec2 = openSet.poll()

        if (cheapestNode == end) {
            // target found, we have a path
            val path = mutableListOf(cheapestNode)

            var node = cheapestNode
            while (parent[node] != null) {
                node = parent[node]!!
                path.add(node)
            }
            return path.reversed()
        }

        closeSet.add(cheapestNode)

        // get the neighbors
        //  for each point, set the cost, and a pointer back if we set the cost
        for (it in neighbors(cheapestNode)) {
            if (it.x < 0 || it.y < 0 || it.x >= size.x || it.y >= size.y)
                continue

            if (closeSet.contains(it))
                continue

            val nextCost = costs[cheapestNode] + cost(it)

            if (nextCost < costs[it]) {
                costs[it] = nextCost
                fScore[it] = nextCost + heuristic(it, end)
                parent[it] = cheapestNode


                if (closeSet.contains(it)) {
                    closeSet.remove(it)
                }
                openSet.add(it)
            }
        }
    }

    // could not find a path
    return null

}
fun dijkstra(size: Vec2, goals: List<Vec2>, blocked: (p: Vec2) -> Boolean): Matrix2d<Int> {
    // clear out map
    val map = matrix2dOf(size.x, size.y) { x, y -> Int.MAX_VALUE }
    val frontier = PriorityQueue<Vec2>(goals.size) { x, y -> map[x].compareTo(map[y]) }

    // put all the points in the frontier
    for (goal in goals) {
        map[goal] = 0
        frontier.add(goal)
    }


    while (frontier.isNotEmpty()) {
        val evaluating = frontier.poll()!!

        val newVal = map[evaluating] + 1
        evaluating.vonNeumanNeighborhood().forEach {
            if (map.contains(it) && map[it] > newVal && !blocked(it)) {
                map[it] = newVal
                frontier.add(it)
            }
        }

    }

    return map
}
class Matrix2d<T>(val xSize: Int, val ySize: Int, val data: Array<T>) {

    operator fun get(p: Vec2): T = get(p.x, p.y)
    operator fun get(x: Int, y: Int): T {
        return data[x + y * xSize]
    }
    operator fun set(p: Vec2, value: T) = set(p.x, p.y, value)
    operator fun set(x: Int, y: Int, value: T) {
        data[x + y * xSize] = value
    }
    fun contains(p: Vec2): Boolean = contains(p.x, p.y)
    fun contains(x: Int, y: Int): Boolean = !outside(x, y)
    fun outside(p: Vec2): Boolean = outside(p.x, p.y)
    fun outside(x: Int, y: Int): Boolean = (x < 0 || y < 0 || x >= xSize || y >= ySize)
    inline fun forEach(function: (T) -> Unit) = data.forEach(function)
    inline fun forEach(func: (x: Int, y: Int) -> Unit) {
        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                func(x, y)
            }
        }
    }
    inline fun forEachIndexed(function: (Int, Int, T) -> Unit) = data.forEachIndexed { i, t ->
        function(
            i % xSize,
            i / xSize,
            t
        )
    }
    fun getSize() = Vec2(xSize, ySize)
    fun setFromList(list: List<T>) {
        list.forEachIndexed { index, t -> data[index] = t }
    }
    fun <R : Comparable<R>> sortedBy(function: (T) -> R?): List<T> = data.sortedBy(function)

    inline fun <reified R> map(transform: (T) -> R): Matrix2d<R> = matrix2dOf(xSize, ySize, data.map(transform))

}
inline fun <reified T> matrix2dOf(size: Vec2, init: (Int, Int) -> T) = matrix2dOf(size.x, size.y, init)
inline fun <reified T> matrix2dOf(xSize: Int, ySize: Int, init: (Int, Int) -> T) =
    Matrix2d(xSize, ySize, Array(xSize * ySize) { i -> init(i % xSize, i / xSize) })
inline fun <reified T> matrix2dOf(xSize: Int, ySize: Int, dataList: List<T>): Matrix2d<T> =
    Matrix2d(xSize, ySize, Array(dataList.size) { i -> dataList[i] })

data class Pos(val x:Int, val y:Int){
    override fun toString(): String {
        return "(x:$x,y:$y)"
    }
    operator fun Pos.plus(move: Move): Pos = copy(x + move.dx, y + move.dy)
    operator fun Pos.minus(point2: Pos): Move = Move(x-point2.x, y-point2.y)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pos) return false
        return x == other.x && y == other.y
    }
}
data class Move(val dx:Int, val dy:Int)
data class Point(val x: Double, val y: Double) {
    operator fun plus(p: Point) = Point(x + p.x, y + p.y)
    operator fun minus(p: Point) = Point(x - p.x, y - p.y)
    operator fun times(p: Point) = Point(x * p.x, y * p.y)
    operator fun div(p: Point) = Point(x / p.x, y / p.y)
    operator fun inc() = Point(x + 1, y + 1)
    operator fun dec() = Point(x - 1, y - 1)
    override fun toString(): String {
        return "(x:$x,y:$y)"
    }
}
enum class Facing(val offset: Vec2) {

    NORTH(Vec2(0, -1)),
    NORTHEAST(Vec2(1, -1)),
    EAST(Vec2(1, 0)),
    SOUTHEAST(Vec2(1, -1)),
    SOUTH(Vec2(0, 1)),
    SOUTHWEST(Vec2(-1, 1)),
    WEST(Vec2(-1, 0)),
    NORTHWEST(Vec2(-1, 1));

    fun left(): Facing = entries[(this.ordinal + 1) % entries.size]
    fun right(): Facing = entries[(this.ordinal - 1 + entries.size) % entries.size]

    fun reverse(): Facing {
        return entries[(ordinal + 4) % entries.size]
    }
}
fun extractOperation(input: String): (Long) -> Long{
    val (_, operator, op2) = input.split(" ")

    return  when(operator){
        "+" -> {old -> old + (op2.toLongOrNull()?: old)}
        "*" -> {old -> old * (op2.toLongOrNull()?: old)}
        else -> error(input)
    }
}
enum class CardinalFacing(val offset: Vec2){
    NORTH(Vec2(0, -1)),
    EAST(Vec2(1, 0)),
    SOUTH(Vec2(0, 1)),
    WEST(Vec2(-1, 0));

    fun left(): Facing = Facing.entries[(this.ordinal + 1) % Facing.entries.size]
    fun right(): Facing = Facing.entries[(this.ordinal - 1 + Facing.entries.size) % Facing.entries.size]

    fun reverse(): Facing {
        return Facing.entries[(ordinal + 2) % Facing.values().size]
    }
}

data class Vec2(val x: Int, val y: Int) {

    operator fun plus(dir: Vec2) = Vec2(x + dir.x, y + dir.y)
    operator fun plus(dir: Facing) = Vec2(x + dir.offset.x, y + dir.offset.y)
    operator fun plus(dir: CardinalFacing) = Vec2(x + dir.offset.x, y + dir.offset.y)

    operator fun minus(dir: Vec2) = Vec2(x - dir.x, y - dir.y)
    operator fun minus(dir: Facing) = Vec2(x - dir.offset.x, y - dir.offset.y)

    // Includes the center point
    fun inclusiveVonNeumanNeighborhood(): List<Vec2> = listOf(
        this,
        Vec2(x, y + 1),
        Vec2(x + 1, y),
        Vec2(x, y - 1),
        Vec2(x - 1, y)
    )

    // Excludes center point
    fun vonNeumanNeighborhood(): List<Vec2> = listOf(
        Vec2(x, y + 1),
        Vec2(x + 1, y),
        Vec2(x, y - 1),
        Vec2(x - 1, y)
    )

    // Includes the center point
    fun inclusiveMooreNeighborhood(): List<Vec2> = List(9) { index ->
        Vec2(index / 3 - 1 + x, index % 3 - 1 + y)
    }

    // Excludes center point
    fun mooreNeighborhood(): List<Vec2> = List(8) { index ->
        if (index >= 4)
            Vec2((index + 1) % 3 - 1 + x, (index + 1) / 3 - 1 + y)
        else
            Vec2(index % 3 - 1 + x, index / 3 - 1 + y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}

fun getManhattanDistance(pos1: Vec2, pos2: Vec2): Int = getManhattanDistance(pos1.x, pos1.y, pos2.x, pos2.y)
fun getManhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = (abs(x1 - x2) + abs(y1 - y2))
fun getChebyshevDistance(pos1: Vec2, pos2: Vec2): Int = getChebyshevDistance(pos1.x, pos1.y, pos2.x, pos2.y)
fun getChebyshevDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = max(abs(x1 - x2), abs(y1 - y2))
fun getEuclideanDistance(pos1: Vec2, pos2: Vec2): Double = getEuclideanDistance(pos1.x.toDouble(), pos1.y.toDouble(), pos2.x.toDouble(), pos2.y.toDouble())
fun getEuclideanDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double = sqrt((x1 - x2).pow(2.0) + (y1 - y2).pow(2.0))

private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}
private fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}