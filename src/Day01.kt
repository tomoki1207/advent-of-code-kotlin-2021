fun main() {
    fun part1(input: List<String>): Int {
        return input.windowed(2)
            .filter {
                val prev = it[0].toInt()
                val cur = it[1].toInt()
                prev < cur
            }.size
    }

    fun part2(input: List<String>): Int {
        return input.windowed(3)
            .map { listOf(it[0].toInt(), it[1].toInt(), it[2].toInt()).sum().toString() }
            .toList()
            .let { part1(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
