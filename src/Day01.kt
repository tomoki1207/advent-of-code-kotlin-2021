fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
            .zipWithNext()
            .count { (prev, curr) -> prev < curr }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
            .windowed(3) { it.sum() }
            .map { it.toString() }
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
