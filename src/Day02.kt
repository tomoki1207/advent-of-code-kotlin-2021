fun main() {
    data class Position(val horizontal: Int = 0, val depth: Int = 0, val aim: Int = 0) {
        fun shift(horizontal: Int = 0, depth: Int = 0, aim: Int = 0): Position {
            return Position(this.horizontal + horizontal, this.depth + depth, this.aim + aim)
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }
            .fold(Position()) { acc, (command, amount) ->
                when (command) {
                    "forward" -> acc.shift(horizontal = amount)
                    "down" -> acc.shift(depth = amount)
                    "up" -> acc.shift(depth = -amount)
                    else -> throw Exception()
                }
            }.let { it.horizontal * it.depth }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }
            .fold(Position()) { acc, (command, amount) ->
                when (command) {
                    "forward" -> acc.shift(
                        horizontal = amount,
                        depth = acc.aim * amount
                    )
                    "down" -> acc.shift(aim = amount)
                    "up" -> acc.shift(aim = -amount)
                    else -> throw Exception()
                }
            }.let { it.horizontal * it.depth }
    }

    val testInput = readInput("Day02_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}