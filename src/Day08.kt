fun main() {

    fun mapping(signal: List<String>): List<Set<Char>> {
        val byLength = signal.groupBy { it.length }

        val one = byLength[2]!!.first().toCharArray().toSet()
        val four = byLength[4]!!.first().toCharArray().toSet()
        val seven = byLength[3]!!.first().toCharArray().toSet()
        val eight = byLength[7]!!.first().toCharArray().toSet()

        val zeroOrSixOrNine = byLength[6]!!.map { it.toCharArray().toSet() }
        val (nine, zeroOrSix) = zeroOrSixOrNine.partition { it.containsAll(four) }
        val (zero, six) = zeroOrSix.partition { it.containsAll(seven) }

        val twoOrThreeOrFive = byLength[5]!!.map { it.toCharArray().toSet() }
        val (three, twoOrFive) = twoOrThreeOrFive.partition { it.containsAll(seven) }
        val vTopRight = eight - six.first()
        val (two, five) = twoOrFive.partition { it.containsAll(vTopRight) }

        return listOf(
            zero.first(),
            one,
            two.first(),
            three.first(),
            four,
            five.first(),
            six.first(),
            seven,
            eight,
            nine.first()
        )
    }

    fun part1(input: List<String>): Int {
        val outputs = input.map { it.split(" | ").last() }.map { it.split(" ") }
        return outputs.sumOf { output -> output.count { it.length != 5 && it.length != 6 } }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { entry ->
            val inputAndOutput = entry.split(" | ")
            val mapping = mapping(inputAndOutput.first().split(" "))
            inputAndOutput.last().split(" ").map {
                val output = it.toCharArray().toSet()
                mapping.indexOfFirst { digits -> digits == output }
            }.joinToString("").toInt()
        }
    }

    val testInput = readInput("Day08_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}