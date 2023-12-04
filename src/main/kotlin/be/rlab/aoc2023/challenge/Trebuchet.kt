package be.rlab.aoc2023.challenge

import be.rlab.aoc2023.support.ResourceUtils.loadInput

/** This challenge is about grouping numbers and applying rules on the sum of each group.
 *
 * It uses a list of lists to represent the different groups in the inventory. This data structure is
 * really simple, and we can use the collections API to sum and sort the groups of integers.
 *
 * @link https://adventofcode.com/2023/day/1
 */


fun main() {
    val digitNames = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val digitNameTest = Regex(digitNames.joinToString("|"))
    val calibrationTable: List<String> = loadInput("01-trebuchet.txt").split("\n")
    val calibrationValues: List<Int> = calibrationTable.map { line: String ->
        val digits: MutableList<Int> = mutableListOf()

        line.fold("") { buffer, char ->
            val nextBuffer = "$buffer$char"
            val digit: Int = if (char.isDigit()) {
                char.digitToInt()
            } else {
                digitNames.indexOf(digitNameTest.find(nextBuffer)?.value)
            }
            if (digit > -1) {
                digits += digit
                char.toString()
            } else {
                nextBuffer
            }
        }
        "${digits.first()}${digits.last()}".toInt()
    }

    println("calibration value: ${calibrationValues.sum()}")
}
