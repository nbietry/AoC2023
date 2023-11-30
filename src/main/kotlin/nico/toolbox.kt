package nico
import java.util.regex.Matcher
import java.util.regex.Pattern

fun extractNumbers(input: String): MutableList<Long> {
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
        if (start == finish) 0
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

data class Pos(val x:Int, val y:Int){
    override fun toString(): String {
        return "(x:$x,y:$y)"
    }
    operator fun Pos.plus(move: Move): Pos = copy(x + move.dx, y + move.dy)
    operator fun Pos.minus(point2: Pos): Move = Move(x-point2.x, y-point2.y)
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