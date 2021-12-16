import java.util.*

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun up() = Point(x, y - 1)
        fun down() = Point(x, y + 1)
        fun left() = Point(x - 1, y)
        fun right() = Point(x + 1, y)
    }

    data class Position(val point: Point, val risk: Int)
    data class Destination(val position: Position, var totalRisk: Int)

    fun initDest(input: List<List<Char>>): Map<Point, Destination> {
        return input.indices.flatMap { y ->
            input[y].indices.map { x ->
                val risk = input[y][x].digitToInt()
                val point = Point(x, y)
                point to Destination(Position(point, risk), if (x == 0 && y == 0) 0 else Int.MAX_VALUE)
            }
        }.toMap()
    }

    fun expand(input: List<List<Char>>): List<List<Char>> {
        val times = 5

        // horizontal
        val expanded = input.map { chars ->
            (0 until times).flatMap { n ->
                chars.map { c ->
                    val i = (c.digitToInt() + n) % 9
                    if (0 != i) i else 9
                }
            }
        }

        // vertical
        return (0 until times).flatMap { n ->
            expanded.map { line ->
                line.map { c ->
                    val i = (c + n) % 9
                    (if (0 != i) i else 9).digitToChar()
                }
            }
        }
    }

    fun dijkstra(inputs: List<List<Char>>): Map<Point, Destination> {
        val destinations = initDest(inputs)
        val unresolved = PriorityQueue<Destination> { d1, d2 -> d1.totalRisk - d2.totalRisk }
        unresolved.addAll(destinations.values)
        while (unresolved.isNotEmpty()) {
            val points = unresolved
                .groupBy { it.totalRisk }
                .minByOrNull { it.key }!!.value
            points.forEach { dest ->
                val point = dest.position.point
                listOf(point.up(), point.down(), point.left(), point.right()).forEach { adjacent ->
                    val toDest = destinations[adjacent] ?: return@forEach
                    val risk = dest.totalRisk + toDest.position.risk
                    if (risk < toDest.totalRisk) {
                        toDest.totalRisk = risk
                    }
                }
                unresolved.remove(dest)
            }
        }
        return destinations
    }

    fun part1(input: List<String>): Int {
        val destinations = dijkstra(input.map { it.toList() })
        val bottomRight = Point(input.first().length - 1, input.size - 1)
        return destinations[bottomRight]!!.totalRisk
    }

    fun part2(input: List<String>): Int {
        val destinations = dijkstra(expand(input.map { it.toList() }))
        val bottomRight = Point(input.first().length * 5 - 1, input.size * 5 - 1)
        return destinations[bottomRight]!!.totalRisk
    }

    val testInput = readInput("Day15_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}