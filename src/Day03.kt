typealias ReportLines = List<List<Char>>

enum class Common {
    MOST, LEAST
}

fun main() {

    fun <R> common(
        report: ReportLines,
        seekIndex: Int,
        kind: Common,
        after: (Pair<Int, ReportLines>) -> R
    ): R {
        val (one, zero) = report.partition { it[seekIndex] == '1' }
        val common = if (kind == Common.MOST) (one.size >= zero.size) else (one.size < zero.size)
        return (if (common) Pair(1, one) else Pair(0, zero)).let(after)
    }

    fun part1(input: List<String>): Int {
        val report = input.map { it.toList() }
        val gammaStr = (0 until report[0].size)
            .map { common(report, it, Common.MOST) { (bin, _) -> bin } }
            .joinToString("")

        val mask = "1".repeat(gammaStr.length).toInt(2)
        val gamma = gammaStr.toInt(2)
        val epsilon = gamma xor mask
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val report = input.map { it.toList() }
        val len = report[0].size
        fun findRating(kind: Common): Int {
            var kept = report
            for (i in 0 until len) {
                kept = common(kept, i, kind) { (_, lines) -> lines }
                if (kept.size <= 1) break
            }
            return kept[0].joinToString("").toInt(2)
        }

        return findRating(Common.MOST) * findRating(Common.LEAST)
    }

    val testInput = readInput("Day03_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}