sealed class Packet(val ver: Int, val type: Int, val size: Int) {
    open val versions: Int
        get() = this.ver
    abstract val value: Long
}

class Literal(ver: Int, type: Int, size: Int, private val literalValue: Long) : Packet(ver, type, size) {
    override val value: Long
        get() = literalValue
}

class Operator(ver: Int, type: Int, size: Int, private val sub: List<Packet>) : Packet(ver, type, size) {
    override val versions: Int
        get() = this.ver + this.sub.sumOf { it.versions }
    override val value: Long
        get() = when (type) {
            0 -> sub.sumOf { it.value }
            1 -> sub.fold(1) { acc, packet -> acc * packet.value }
            2 -> sub.minOf { it.value }
            3 -> sub.maxOf { it.value }
            5 -> if (sub.first().value > sub.last().value) 1 else 0
            6 -> if (sub.first().value < sub.last().value) 1 else 0
            7 -> if (sub.first().value == sub.last().value) 1 else 0
            else -> throw Error()
        }
}

fun binToInt(bins: List<Char>): Int = bins.joinToString("").toInt(2)

fun parseLiteral(binary: List<Char>): Literal {
    val ver = binToInt(binary.take(3))
    val type = binToInt(binary.drop(3).take(3))
    val body = binary.drop(6)

    val packets = mutableListOf<String>()
    var pos = 6
    for (i in 0 until body.size - 4 step 5) {
        packets.add(body.subList(i + 1, i + 5).joinToString(""))
        pos += 5
        if (body[i] == '0') {
            break
        }
    }
    val value = packets.joinToString("").toLong(2)
    return Literal(ver, type, pos, value)
}

fun parseOperator(binary: List<Char>): Operator {
    val ver = binToInt(binary.take(3))
    val type = binToInt(binary.drop(3).take(3))
    val lengthType = binary[6]
    val body = binary.drop(7)
    var size = 7

    val subPackets = mutableListOf<Packet>()
    if (lengthType == '0') {
        val length = binToInt(body.take(15))
        val subPart = body.drop(15).take(length)
        var pos = 0
        while (pos < length) {
            val subPacket = parsePacket(subPart.drop(pos))
            subPackets.add(subPacket)
            pos += subPacket.size
        }
        size += 15 + pos
    } else {
        val packetCount = binToInt(body.take(11))
        val subPart = body.drop(11)
        var pos = 0
        repeat(packetCount) {
            val subPacket = parsePacket(subPart.drop(pos))
            subPackets.add(subPacket)
            pos += subPacket.size
        }
        size += 11 + pos
    }
    return Operator(ver, type, size, subPackets)
}

fun parsePacket(binary: List<Char>): Packet {
    return when (binToInt(binary.drop(3).take(3))) {
        4 -> {
            parseLiteral(binary)
        }
        else -> {
            parseOperator(binary)
        }
    }
}

fun main() {

    fun parse(raw: String): Packet {
        val binary = raw.flatMap { it.digitToInt(16).toString(2).padStart(4, '0').toList() }
        return parsePacket(binary)
    }

    fun part1(input: List<String>): List<Int> {
        return input.map { parse(it) }
            .map { it.versions }
    }

    fun part2(input: List<String>): List<Long> {
        return input.map { parse(it) }
            .map { it.value }
    }

    val testInput = readInput("Day16_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}