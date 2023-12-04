package be.rlab.aoc2023.challenge

import be.rlab.aoc2023.support.ResourceUtils.loadInput

data class Scratchcard(
    val id: Int,
    val winningNumbers: List<Int>,
    val selectedNumbers: List<Int>
) {
    val matchCount: Int = winningNumbers.filter { value ->
        selectedNumbers.contains(value)
    }.size

    val worth: Int = if (matchCount > 0) {
        (1 until matchCount).fold(1) { total, _ ->
            total * 2
        }
    } else {
        matchCount
    }
}

fun parseScratchcards(): List<Scratchcard> {
    return loadInput("04-scratchcards.txt").split("\n").map { line ->
        val id: Int = line.substringBefore(":").substringAfter("Card").trim().toInt()
        val numbersSets = line.substringAfter(":").split("|")
        val winningNumbers: List<Int> = numbersSets.first().trim().split(" ").toList()
            .filter { it.trim().isNotBlank() }
            .map { value -> value.toInt() }
        val selectedNumbers: List<Int> = numbersSets.last().trim().split(" ").toList()
            .filter { it.trim().isNotBlank() }
            .map { value -> value.toInt() }
        Scratchcard(id = id, winningNumbers = winningNumbers, selectedNumbers = selectedNumbers)
    }
}

fun main() {
    val cards: List<Scratchcard> = parseScratchcards()
    val worth: Int = cards.sumOf { it.worth }
    println("cards total worth: $worth")

    val candidates: ArrayDeque<Scratchcard> = ArrayDeque(cards)
    var total = candidates.size

    while (candidates.isNotEmpty()) {
        val card = candidates.removeFirst()
        val copies = cards.subList(card.id, card.id + card.matchCount)
        candidates += copies
        total += copies.size
    }

    println("total copies: $total")
}
