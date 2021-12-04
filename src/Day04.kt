typealias BoardLine = List<Int>

fun main() {

    class Board(private val rows: List<BoardLine>) {
        private val winPatterns: List<BoardLine>
        private val drawn = mutableListOf<Int>()

        init {
            val patterns = mutableListOf<BoardLine>()

            // horizontal
            patterns.addAll(rows)

            // vertical
            patterns.addAll((0 until 5).map { i -> rows.map { it[i] } })

            // cross
            // numbers.mapIndexed { i, line -> line[i] }.also { lines.add(it) }
            // numbers.reversed().mapIndexed { i, line -> line[i] }.also { lines.add(it) }

            this.winPatterns = patterns
        }

        fun mark(num: Int): Boolean {
            drawn.add(num)
            // return BINGO or not
            return winPatterns.any { drawn.containsAll(it) }
        }

        fun unmarked() = rows.flatten().filterNot { drawn.contains(it) }
    }

    fun prepare(input: List<String>): Pair<List<Int>, List<Board>> {
        // drawn numbers
        val nums = input.first().split(",").map { it.toInt() }

        // boards
        val boards = input.drop(1)
            .filterNot { it.isEmpty() }
            .chunked(5)
            .map { boardLines ->
                boardLines.map { line ->
                    line.split("  ", " ").filterNot { it.isEmpty() }.map { it.toInt() }
                }.let { Board(it) }
            }

        return Pair(nums, boards)
    }

    fun part1(input: List<String>): Int {
        val (draws, boards) = prepare(input)
        for (drawn in draws) {
            boards.firstOrNull { it.mark(drawn) }?.also {
                return it.unmarked().sum() * drawn
            }
        }
        throw Exception()
    }

    fun part2(input: List<String>): Int {
        val (draws, boards) = prepare(input)
        val playing = boards.toMutableList()
        for (drawn in draws) {
            val bingo = playing.filter { it.mark(drawn) }
            if (bingo.size == 1 && playing.size == 1) {
                return bingo[0].unmarked().sum() * drawn
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