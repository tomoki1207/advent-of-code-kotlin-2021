fun main() {
    fun part1(input: List<String>): Int {
        val heightMap = input.map { line -> line.toCharArray().map { it.digitToInt() } }
        return (heightMap.indices).flatMap { i ->
            val row = heightMap[i]
            (row.indices).mapNotNull { j ->
                val point = row[j]
                val top = heightMap.getOrNull(i - 1)?.get(j) ?: 10
                val down = heightMap.getOrNull(i + 1)?.get(j) ?: 10
                val left = row.getOrNull(j - 1) ?: 10
                val right = row.getOrNull(j + 1) ?: 10
                if (point < top && point < down && point < left && point < right) point else null
            }
        }.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        // collect basins consider row direction only
        val rowBasins = input.mapIndexed { index, line ->
            val basinIndexes = mutableListOf<MutableSet<Int>>()
            var startIndex = 0
            for (nonNine in line.split(Regex("9+"))) {
                val start = line.indexOf(nonNine, startIndex = startIndex)
                val end = start + nonNine.length - 1
                basinIndexes.add((start..end).toMutableSet())
                startIndex = start + nonNine.length
            }
            index to basinIndexes
        }.toMap()

        // merge row basins
        val basins = rowBasins[0]!!.map { mutableMapOf(0 to it) }.toMutableList()
        for (i in 1 until rowBasins.size) {
            val upper = i - 1
            val candidates = basins.filter { it.containsKey(upper) }
            rowBasins[i]!!.forEach { rowBasin ->
                val sameBasins = candidates.filter { rowBasin.intersect(it[upper]!!).isNotEmpty() }
                when (sameBasins.size) {
                    0 ->
                        // begin new basin
                        basins.add(mutableMapOf(i to rowBasin))
                    1 ->
                        // found single basin to merge
                        sameBasins[0][i]?.plusAssign(rowBasin) ?: sameBasins[0].put(i, rowBasin)
                    else -> {
                        // found multiple basins to merge,
                        // merge into first basin
                        val first = sameBasins.first()
                        first[i]?.plusAssign(rowBasin) ?: first.put(i, rowBasin)
                        sameBasins.drop(1).forEach { other ->
                            other.entries.forEach { (key, set) ->
                                first[key]?.plusAssign(set) ?: first.put(key, set)
                            }
                            basins.remove(other)
                        }
                    }
                }
            }
        }

        return basins.map { it.values.flatten().size }
            .sortedByDescending { it }
            .take(3)
            .fold(1) { acc, size -> acc * size }
    }

    val testInput = readInput("Day09_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}