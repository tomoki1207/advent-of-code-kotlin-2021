fun main() {

    data class Point(val x: Int, val y: Int)

    data class Line(val from: Point, val to: Point) {
        val covers: List<Point>
        val horizontal = from.y == to.y
        val vertical = from.x == to.x

        init {
            val covers = mutableListOf<Point>()
            val dx = to.x - from.x
            val dy = to.y - from.y
            // consider only if slope==1
            val increaseX = (1 * if (dx == 0) 0 else if (dx < 0) -1 else 1)
            val increaseY = (1 * if (dy == 0) 0 else if (dy < 0) -1 else 1)

            var x = from.x
            var y = from.y
            while (x != to.x + increaseX || y != to.y + increaseY) {
                covers.add(Point(x, y))
                x += increaseX
                y += increaseY
            }

            this.covers = covers.distinct()
        }
    }

    fun extract(input: List<String>) = input.map { line ->
        line.split(" -> ").map { point ->
            point.split(",").let { Point(it.first().toInt(), it.last().toInt()) }
        }.let { Line(it.first(), it.last()) }
    }

    fun part1(input: List<String>): Int {
        val lines = extract(input)
        val overlaps = lines
            .filter { it.horizontal || it.vertical }
            .flatMap { it.covers }
            .groupingBy { it }
            .eachCount()
        return overlaps.filter { it.value >= 2 }.count()
    }

    fun part2(input: List<String>): Int {
        val lines = extract(input)
        val overlaps = lines
            .flatMap { it.covers }
            .groupingBy { it }
            .eachCount()
        return overlaps.filter { it.value >= 2 }.count()
    }

    val testInput = readInput("Day05_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}