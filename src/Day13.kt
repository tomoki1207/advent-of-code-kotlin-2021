fun main() {
    data class Point(val x: Int, val y: Int)

    class Fold(command: String) {
        val axis: String
        val amount: Int

        init {
            val (axis, amount) = command.split(" ").last().split("=")
            this.axis = axis
            this.amount = amount.toInt()
        }

        fun fold(point: Point) = when (axis) {
            "x" ->
                if (point.x < amount) point else Point(point.x - ((point.x - amount) * 2), point.y)
            "y" ->
                if (point.y < amount) point else Point(point.x, point.y - ((point.y - amount) * 2))
            else ->
                throw Error()
        }
    }

    fun prepare(input: List<String>): Pair<List<Point>, List<Fold>> {
        val points = input.takeWhile { it.isNotBlank() }
            .map { line ->
                line.split(",")
                    .let { Point(it.first().toInt(), it.last().toInt()) }
            }
        val commands = input.takeLastWhile { it.isNotBlank() }
            .map { Fold(it) }
        return Pair(points, commands)
    }

    fun part1(input: List<String>): Int {
        val (points, folds) = prepare(input)
        val firstFold = folds.first()
        return points.map { firstFold.fold(it) }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val (points, folds) = prepare(input)
        val result = folds.fold(points.toSet()) { acc, command ->
            acc.map { command.fold(it) }.toSet()
        }
        for (y in 0..result.maxOf { it.y }) {
            for (x in 0..result.maxOf { it.x }) {
                result.contains(Point(x, y)).let { if (it) "#" else " " }.let { print(it) }
            }
            println()
        }
        return result.size
    }

    val testInput = readInput("Day13_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}