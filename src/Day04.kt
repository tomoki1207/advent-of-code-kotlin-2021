typealias BoardLine = List<Int>

fun main() {

    class Board(private val numbers: List<BoardLine>) {
        private val lines: List<BoardLine>
        private val drawn = mutableListOf<Int>()

        init {
            val lines = mutableListOf<BoardLine>()

            // horizontal
            lines.addAll(numbers)

            // vertical
            lines.addAll((0 until 5).map { i -> numbers.map { it[i] } })

            // cross
            // numbers.mapIndexed { i, line -> line[i] }.also { lines.add(it) }
            // numbers.reversed().mapIndexed { i, line -> line[i] }.also { lines.add(it) }

            this.lines = lines
        }

        fun mark(num: Int): Boolean {
            drawn.add(num)
            // return BINGO or not
            return lines.any { drawn.containsAll(it) }
        }

        fun unmarked() = numbers.flatten().filterNot { drawn.contains(it) }
    }

    fun extract(input: List<String>): Pair<List<Int>, List<Board>> {
        // drawn numbers
        val nums = input.first().split(",").map { it.toInt() }

        // boards each 6 lines - 5 board lines and a blank line
        val boards = mutableListOf<Board>()
        for (i in 1 until input.size step 6) {
            input.subList(i + 1, i + 6)
                .map { line -> line.split("  ", " ").filterNot { it.isEmpty() }.map { it.toInt() } }
                .also { boards.add(Board(it)) }
        }
        return Pair(nums, boards)
    }

    fun part1(input: List<String>): Int {
        val (draws, boards) = extract(input)
        for (drown in draws) {
            boards.firstOrNull { it.mark(drown) }?.also {
                return it.unmarked().sum() * drown
            }
        }
        throw Exception()
    }

    fun part2(input: List<String>): Int {
        val (draws, boards) = extract(input)
        val playing = boards.toMutableList()
        for (drown in draws) {
            val bingo = playing.filter { it.mark(drown) }
            if (bingo.size == 1 && playing.size == 1) {
                return bingo[0].unmarked().sum() * drown
            }
            playing.removeAll(bingo)
        }
        throw Exception()
    }

    val testInput = readInput("Day04_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}