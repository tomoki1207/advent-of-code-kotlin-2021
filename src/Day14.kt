fun main() {
    fun prepare(input: List<String>): Pair<String, Map<String, Char>> {
        val template = input.first()
        val pairs = input.drop(2)
            .associate { line ->
                line.split(" -> ").let { it.first() to it.last().first() }
            }
        return Pair(template, pairs)
    }

    fun countAppearance(template: String, rules: Map<String, Char>, steps: Int): Map<Char, Long> {
        var pairCounts = template.windowed(2)
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        repeat(steps) {
            pairCounts = pairCounts.flatMap { (pair, count) ->
                val insert = rules[pair]!!
                listOf("${pair.first()}$insert" to count, "$insert${pair.last()}" to count)
            }
                .groupingBy { it.first }
                .fold(0L) { acc, (_, count) -> acc + count }
        }

        val appearance = mutableMapOf(template.first() to 1L)
        pairCounts.forEach { (pair, count) ->
            appearance[pair.last()] = appearance[pair.last()]?.plus(count) ?: count
        }
        return appearance
    }

    fun part1(input: List<String>): Long {
        val (template, rules) = prepare(input)
        val appearance = countAppearance(template, rules, 10).values
        return appearance.maxOrNull()!! - appearance.minOrNull()!!
    }

    fun part2(input: List<String>): Long {
        val (template, rules) = prepare(input)
        val appearance = countAppearance(template, rules, 40).values
        return appearance.maxOrNull()!! - appearance.minOrNull()!!
    }

    val testInput = readInput("Day14_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}