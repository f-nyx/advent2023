package be.rlab.aoc2023.challenge

import be.rlab.aoc2023.support.ResourceUtils.loadInput

/** This challenge is about grouping numbers and applying rules on the sum of each group.
 *
 * It uses a list of lists to represent the different groups in the inventory. This data structure is
 * really simple, and we can use the collections API to sum and sort the groups of integers.
 *
 * @link https://adventofcode.com/2023/day/1
 */

data class GameResult(
    val red: Int,
    val green: Int,
    val blue: Int
)

data class Game(
    val id: Int,
    val results: List<GameResult>
)

fun main() {
    val games: List<Game> = loadInput("02-cube_conundrum.txt").split("\n").map { line ->
        val id: Int = line.substringBefore(":").substringAfter("Game").trim().toInt()
        val results: List<GameResult> = line.substringAfter(":").split(";").map { results ->
            val parsedResults: Map<String, Int> = results.split(",").associate { result ->
                val (value, color) = result.trim().split(" ")
                color to value.toInt()
            }
            GameResult(
                red = parsedResults["red"] ?: 0,
                green = parsedResults["green"] ?: 0,
                blue = parsedResults["blue"] ?: 0
            )
        }
        Game(id = id, results = results)
    }
    val idSum = games.filter { game ->
        game.results.none { result ->
            result.red > 12 || result.green > 13 || result.blue > 14
        }
    }.sumOf { game -> game.id }
    println("part1: $idSum")

    val cubePower = games.sumOf { game ->
        val minRed: Int = game.results.maxOf(GameResult::red)
        val minGreen: Int = game.results.maxOf(GameResult::green)
        val minBlue: Int = game.results.maxOf(GameResult::blue)
        minRed * minGreen * minBlue
    }
    println("part 2: $cubePower")
}
