fun main() {
    data class Path(val p1: String, val p2: String) {
        fun has(point: String) = p1 == point || p2 == point
        fun another(point: String) = if (point == p1) p2 else p1
    }

    fun paths(input: List<String>) = input.map { line ->
        val path = line.split("-")
        Path(path.first(), path.last())
    }

    fun route(
        point: String,
        paths: List<Path>,
        decideVisited: (point: String, visited: List<String>) -> Boolean,
        visited: MutableList<String> = mutableListOf()
    ): List<List<String>> {
        visited.add(point)
        if (point == "end") {
            return listOf(visited)
        }

        val nextPoints = paths.filter { it.has(point) }
            .map { it.another(point) }
            .filterNot { decideVisited(it, visited) }

        return nextPoints.flatMap { route(it, paths, decideVisited, visited.toMutableList()) }
    }

    fun part1(input: List<String>): Int {
        return route("start", paths(input), { p, visited ->
            p == p.lowercase() && visited.contains(p)
        }).size
    }

    fun part2(input: List<String>): Int {
        return route("start", paths(input), { p, visited ->
            val lowers = visited.filter { it == it.lowercase() }
            p == "start" || (lowers.contains(p)
                    && lowers.distinct().size != lowers.size)
        }).size
    }

    val testInput = readInput("Day12_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}