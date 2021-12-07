import java.util.*

fun main() {
    fun countFishes(initial: List<Int>, days: Int): Long {
        val timer = MutableList(9) { 0L }

        // init
        for (i in initial) {
            timer[i]++
        }

        // apply days
        repeat(days) {
            Collections.rotate(timer, -1)
            // add reset lanternfish
            timer[6] += timer[8]
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