import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    fun median(list: List<Int>) = list.sorted().let {
        if (it.size % 2 == 1) it[it.size / 2] else (it[it.size / 2] + it[(it.size / 2) - 1]) / 2
    }

    // Sn=(n(2a+(n-1)d)/2
    fun sn(n: Int) = (n * (1 + n)) / 2

    fun part1(input: List<String>): Int {
        val pos = input.first().split(",").map { it.toInt() }
        val med = median(pos)
        return pos.sumOf { abs(it - med) }
    }

    fun part2(input: List<String>): Int {
        val pos = input.first().split(",").map { it.toInt() }
        val ave = pos.average()
        return listOf(ceil(ave).toInt(), floor(ave).toInt()).minOf { p ->
            pos.sumOf { sn(abs(it - p)) }
        }
    }

    val testInput = readInput("Day07_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}