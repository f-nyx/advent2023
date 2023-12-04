package be.rlab.aoc2023.challenge

import be.rlab.aoc2023.support.Grid
import be.rlab.aoc2023.support.Point
import be.rlab.aoc2023.support.ResourceUtils.loadInput

fun parseEngineSchema(): Grid {
    val input = loadInput("03-gear_ratios.txt")
    val width = input.split("\n").first().length
    val points: List<Point> = input.replace("\n", "").mapIndexed { index, point ->
        Point(x = index % width, y = index / width, value = point)
    }
    return Grid(
        height = input.split("\n").size,
        width = width,
        points = points
    )
}

fun calculatePartsScore(schema: Grid): Int {
    return schema.points.fold(Pair(0, 0)) { context, point ->
        val (partsScore, lastPartNumber) = context
        val candidate = schema.neighbors(point).any { neighbor ->
            !neighbor.value.isDigit() && neighbor.value != '.'
        }

        if (point.value.isDigit() && candidate) {
            val partNumber = resolvePartNumber(schema, point)
            if (lastPartNumber != partNumber) {
                (partsScore + partNumber) to partNumber
            } else {
                context
            }
        } else {
            partsScore to 0
        }
    }.first
}

fun calculateGearRatio(schema: Grid): Int {
    return schema.points.fold(0) { gearRatio, point ->
        if (point.value == '*') {
            val neighbors = schema.neighbors(point)
            val numbers = neighbors
                .filter { neighbor -> neighbor.value.isDigit() }
                .map { neighbor -> resolvePartNumber(schema, neighbor) }
                .toSet()
            if (numbers.size == 2) {
                gearRatio + numbers.first() * numbers.last()
            } else {
                gearRatio
            }
        } else {
            gearRatio
        }
    }
}

/** Starting at one digit, it searches digits left and right to compose a number.
 */
fun resolvePartNumber(
    schema: Grid,
    ref: Point
): Int {
    val refIndex = ref.translateToIndex(schema.width)
    var offset = 0
    var value = "${ref.value}"
    var left: Int? = refIndex
    var right: Int? = refIndex
    val points = schema.points

    do {
        offset += 1
        left = (refIndex - offset)
            .takeIf { leftIndex ->
                left != null && leftIndex >= 0 && points[leftIndex].value.isDigit()
            }
            ?.also { leftIndex ->
                value = "${points[leftIndex].value}$value"
            }
        right = (refIndex + offset)
            .takeIf { rightIndex ->
                right != null && rightIndex < points.size && points[rightIndex].value.isDigit()
            }
            ?.also { rightIndex ->
                value = "$value${points[rightIndex].value}"
            }
    } while (left != null || right != null)

    return value.toInt()
}

fun main() {
    val schema: Grid = parseEngineSchema()
    println("total copies: ${calculatePartsScore(schema)}")
    println("gear ratio: ${calculateGearRatio(schema)}")
}
