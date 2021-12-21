fun main() {
    data class Point(val x: Int, val y: Int) {
        fun move(x: Int, y: Int) = Point(this.x + x, this.y + y)
    }

    data class Velocity(val x: Int, val y: Int) {
        fun step() = Velocity(if (x == 0) 0 else if (x > 0) x - 1 else x + 1, y - 1)
    }

    data class Probe(val target: Pair<IntRange, IntRange>) {
        fun simulate(initial: Velocity): Pair<Boolean, List<Point>> {
            fun reached(point: Point) = point.x in target.first && point.y in target.second
            fun overrun(point: Point) = point.x > target.first.last || point.y < target.second.first

            var point = Point(0, 0)
            val points = mutableListOf(point)
            var reached: Boolean
            var velocity = initial
            do {
                point = point.move(velocity.x, velocity.y)
                velocity = velocity.step()
                points.add(point)
                reached = reached(point)
            } while (!(reached || overrun(point)))
            return Pair(reached, points)
        }
    }

    fun prepare(range: String): Pair<IntRange, IntRange> {
        val (x, y) = range.removePrefix("target area: ").split(", ")
        val xRange = x.removePrefix("x=").split("..").let { IntRange(it.first().toInt(), it.last().toInt()) }
        val yRange = y.removePrefix("y=").split("..").let { IntRange(it.first().toInt(), it.last().toInt()) }
        return Pair(xRange, yRange)
    }

    fun part1(input: List<String>): Int {
        val target = prepare(input.first())
        val probe = Probe(target)
        return (1..1000).mapNotNull { x ->
            (1..1000).map { y ->
                probe.simulate(Velocity(x, y))
            }.filter { (reached, _) -> reached }
                .maxOfOrNull { (_, points) -> points.maxOf { it.y } }
        }.maxOf { it }
    }

    fun part2(input: List<String>): Int {
        val target = prepare(input.first())
        val probe = Probe(target)
        return (1..1000).flatMap { x ->
            (-1000..1000).mapNotNull { y ->
                val v = Velocity(x, y)
                val (reached, _) = probe.simulate(v)
                if (reached) v else null
            }
        }.size
    }

    val testInput = readInput("Day17_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}