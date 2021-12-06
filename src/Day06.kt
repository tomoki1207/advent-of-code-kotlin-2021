fun main() {
    fun countFishes(initial: List<Int>, days: Int): Long {
        var timer = MutableList(9) { 0L }

        // init
        for (i in initial) {
            timer[i]++
        }

        // apply days
        for (i in 1..days) {
            val reset = timer[0]
            timer = timer.subList(1, 9)
            // add reset lanternfish
            timer[6] += reset
            // new lanternfish
            timer.add(reset)
        }

        return timer.sum()
    }

    fun prepare(input: List<String>) = input.first().split(",").map { it.toInt() }

    fun part1(input: List<String>): Long {
        return countFishes(prepare(input), 80)
    }

    fun part2(input: List<String>): Long {
        return countFishes(prepare(input), 256)
    }

    val testInput = readInput("Day06_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}