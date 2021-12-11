fun main() {
    fun discard(input: CharArray): Pair<Boolean, Long> {
        val stack = ArrayDeque<Char>()
        input.forEach { c ->
            when (c) {
                ')' -> {
                    val last = stack.removeLast()
                    if (last != '(') {
                        return false to 3
                    }
                }
                ']' -> {
                    val last = stack.removeLast()
                    if (last != '[') {
                        return false to 57
                    }
                }
                '}' -> {
                    val last = stack.removeLast()
                    if (last != '{') {
                        return false to 1197
                    }
                }
                '>' -> {
                    val last = stack.removeLast()
                    if (last != '<') {
                        return false to 25137
                    }
                }
                else -> stack.addLast(c)
            }
        }

        // incomplete
        return true to stack.reversed().fold(0L) { acc, c ->
            acc * 5L + (mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)[c]!!)
        }
    }

    fun part1(input: List<String>): Long {
        return input.map { discard(it.toCharArray()) }
            .filter { !it.first }
            .sumOf { it.second }
    }

    fun part2(input: List<String>): Long {
        val incomplete = input.map { discard(it.toCharArray()) }
            .filter { it.first }
            .map { it.second }
            .sortedBy { it }
        return incomplete[incomplete.size / 2]
    }

    val testInput = readInput("Day10_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}