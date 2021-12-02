fun main() {
    data class Position(var horizontal: Int = 0, var depth: Int = 0, var aim: Int = 0)

    fun part1(input: List<String>): Int {
        val pos = input.map { it.split(" ").let { part -> Pair(part[0], part[1]) } }
            .fold(Position()) { acc, pair ->
                val command = pair.first
                val diff = pair.second.toInt()
                when (command) {
                    "forward" -> acc.horizontal += diff
                    "down" -> acc.depth += diff
                    "up" -> acc.depth -= diff
                }
                acc
            }
        return pos.horizontal * pos.depth
    }

    fun part2(input: List<String>): Int {
        val pos = input.map { it.split(" ").let { part -> Pair(part[0], part[1]) } }
            .fold(Position()) { acc, pair ->
                val command = pair.first
                val diff = pair.second.toInt()
                when (command) {
                    "forward" -> {
                        acc.horizontal += diff
                        acc.depth += diff.times(acc.aim)
                    }
                    "down" -> acc.aim += diff
                    "up" -> acc.aim -= diff
                }
                acc
            }
        return pos.horizontal * pos.depth
    }

    val testInput = readInput("Day02_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}