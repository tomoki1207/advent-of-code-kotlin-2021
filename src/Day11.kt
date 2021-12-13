fun main() {
    data class Point(val x: Int, val y: Int) {
        fun up() = Point(x, y - 1)
        fun down() = Point(x, y + 1)
        fun left() = Point(x - 1, y)
        fun right() = Point(x + 1, y)
        fun upLeft() = up().left()
        fun upRight() = up().right()
        fun downLeft() = down().left()
        fun downRight() = down().right()
    }

    data class Octopus(val init: Int, val onFlashed: () -> Unit) {
        var energyLevel = init

        fun increase() {
            ++energyLevel
            if (energyLevel == 10) {
                // flash
                onFlashed()
            }
        }

        fun coolDown() {
            if (energyLevel > 9) {
                energyLevel = 0
            }
        }
    }

    fun prepare(input: List<String>): Map<Point, Octopus> {
        val map = mutableMapOf<Point, Octopus>()
        val onFlashed = { p: Point ->
            map[p.up()]?.increase()
            map[p.down()]?.increase()
            map[p.left()]?.increase()
            map[p.right()]?.increase()
            map[p.upLeft()]?.increase()
            map[p.upRight()]?.increase()
            map[p.downLeft()]?.increase()
            map[p.downRight()]?.increase()
        }

        input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, c ->
                val point = Point(x, y)
                map[point] = Octopus(c.digitToInt()) { onFlashed(point) }
            }
        }
        return map
    }

    fun part1(input: List<String>): Int {
        val octopuses = prepare(input)
        return (0..99).sumOf {
            octopuses.values.forEach { it.increase() }
            octopuses.values
                .filter { it.energyLevel > 9 }
                .onEach { it.coolDown() }
                .count()
        }
    }

    fun part2(input: List<String>): Int {
        val octopuses = prepare(input)
        var step = 1
        while (true) {
            octopuses.values.forEach { it.increase() }
            val flashed = octopuses.values
                .filter { it.energyLevel > 9 }
                .onEach { it.coolDown() }
                .count()
            if (flashed == 100) {
                return step
            }
            ++step
        }
    }

    val testInput = readInput("Day11_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}