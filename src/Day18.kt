sealed class Node(var parent: PairNode?) {
    open fun shift() {}
    open fun explode() = false
    open fun split() = false
    abstract fun magnitude(): Int
}

class NumNode(parent: PairNode?, var num: Int) : Node(parent) {
    override fun magnitude() = num
    override fun toString() = num.toString()
}

class PairNode(parent: PairNode?, private var left: Node, private var right: Node, private var depth: Int) :
    Node(parent) {

    fun add(another: PairNode): PairNode {
        this.shift()
        another.shift()
        return PairNode(null, this, another, 0).also {
            this.parent = it
            another.parent = it
        }.reduce()
    }

    override fun shift() {
        this.depth += 1
        this.left.shift()
        this.right.shift()
    }

    override fun explode(): Boolean {
        if (left.explode()) {
            return true
        }
        if (right.explode()) {
            return true
        }

        if (depth == 3) {
            if (left is PairNode) {
                val l = left as PairNode
                closestLeftOf(l)?.let { it.num += (l.left as NumNode).num }
                closestRightOf(l)?.let { it.num += (l.right as NumNode).num }
                left = NumNode(this, 0)
                return true
            }
            if (right is PairNode) {
                val r = right as PairNode
                closestLeftOf(r)?.let { it.num += (r.left as NumNode).num }
                closestRightOf(r)?.let { it.num += (r.right as NumNode).num }
                right = NumNode(this, 0)
                return true
            }
        }
        return false
    }

    override fun split(): Boolean {
        if (left.split()) {
            return true
        }
        if (right.split()) {
            return true
        }

        if (left is NumNode && (left as NumNode).num > 9) {
            val num = (left as NumNode).num
            val l = num / 2
            left = PairNode(this, NumNode(this, l), NumNode(this, num - l), depth + 1)
            return true
        }
        if (right is NumNode && (right as NumNode).num > 9) {
            val num = (right as NumNode).num
            val l = num / 2
            right = PairNode(this, NumNode(this, l), NumNode(this, num - l), depth + 1)
            return true
        }
        return false
    }

    override fun magnitude(): Int = this.left.magnitude() * 3 + this.right.magnitude() * 2

    override fun toString() = "[${left},${right}]"

    private fun reduce(): PairNode {
        do {
            println(this.toString())
        } while (explode() || split())
        return this
    }

    private fun closestLeftOf(from: Node): NumNode? {
        if (from == right) {
            return if (left is NumNode) left as NumNode else (left as PairNode).closestLeftOf(this)
        }
        if (from == left) {
            return parent?.closestLeftOf(this)
        }
        return if (right is NumNode) right as NumNode else (right as PairNode).closestLeftOf(this)
    }

    private fun closestRightOf(from: Node): NumNode? {
        if (from == left) {
            return if (right is NumNode) right as NumNode else (right as PairNode).closestRightOf(this)
        }
        if (from == right) {
            return parent?.closestRightOf(this)
        }
        return if (left is NumNode) left as NumNode else (left as PairNode).closestRightOf(this)
    }
}

fun parse(input: String): Node {
    var i = 0
    fun parse(depth: Int = 0, parent: PairNode? = null): Node {
        if (input[i] == '[') {
            // pair
            i++
            val l = parse(depth + 1)
            i++
            val r = parse(depth + 1)
            i++
            return PairNode(parent, l, r, depth).also {
                l.parent = it
                r.parent = it
            }
        }
        // regular num
        val start = i
        while (input[i] in '0'..'9') i++
        return NumNode(parent, input.substring(start, i).toInt())
    }
    return parse()
}

fun main() {

    fun part1(input: List<String>): Int {
        val nums = input.map { parse(it) }
        return nums.drop(1)
            .fold(nums.first()) { acc, num -> (acc as PairNode).add(num as PairNode) }
            .magnitude()
    }

    fun part2(input: List<String>): Int {
        val magnitudes = mutableListOf<Int>()
        for (first in input) {
            for (second in input) {
                if (first == second) {
                    continue
                }
                val f = parse(first) as PairNode
                val s = parse(second) as PairNode
                magnitudes += f.add(s).magnitude()
            }
        }
        return magnitudes.maxOrNull()!!
    }

    val testInput = readInput("Day18_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}