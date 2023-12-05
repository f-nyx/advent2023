package be.rlab.aoc2023.challenge

import be.rlab.aoc2023.support.ResourceUtils.loadInput

data class Mapper(
    val name: String,
    val sourceRange: LongRange,
    val destinationRange: LongRange
)

data class Instructions(
    val seeds: List<Long>,
    val mappers: List<Mapper>
) {
    fun mapSoil(seed: Long): Long = mapBy("seed-to-soil", seed)
    fun mapFertilizer(soil: Long): Long = mapBy("soil-to-fertilizer", soil)
    fun mapWater(fertilizer: Long): Long = mapBy("fertilizer-to-water", fertilizer)
    fun mapLight(water: Long): Long = mapBy("water-to-light", water)
    fun mapTemperature(light: Long): Long = mapBy("light-to-temperature", light)
    fun mapHumidity(temperature: Long): Long = mapBy("temperature-to-humidity", temperature)
    fun mapLocation(humidity: Long): Long = mapBy("humidity-to-location", humidity)

    private fun mapBy(mapperName: String, source: Long): Long {
        return mappers
            .filter { mapper -> mapper.name == mapperName }
            .find { mapper ->
                source in mapper.sourceRange
            }?.let { mapper ->
                val offset = mapper.sourceRange.last - source
                mapper.destinationRange.last - offset
            } ?: source
    }
}

fun parseInstructions(): Instructions {
    val instructions = loadInput("05-food_production.txt").split("\n\n")
    val seeds: List<Long> = instructions.first().substringAfter("seeds:").trim().split(" ").map { it.toLong() }
    val mappers: List<Mapper> = instructions.drop(1).flatMap { category ->
        val lines = category.split("\n")
        val name: String = lines.first().substringBeforeLast("map:").trim()
        lines.drop(1).map { line ->
            val (destination, source, length) = line.split(" ")
            Mapper(
                name = name,
                sourceRange = source.toLong() until (source.toLong() + length.toLong()),
                destinationRange = destination.toLong() until (destination.toLong() + length.toLong())
            )
        }
    }
    return Instructions(seeds, mappers)
}

fun main() {
    val instructions: Instructions = parseInstructions()
    val productionPipeline: List<(Long) -> Long> = listOf(
        instructions::mapSoil,
        instructions::mapFertilizer,
        instructions::mapWater,
        instructions::mapLight,
        instructions::mapTemperature,
        instructions::mapHumidity,
        instructions::mapLocation
    )

    val locations = instructions.seeds.map { seed ->
        productionPipeline.fold(seed) { result, mapper ->
            mapper(result)
        }
    }
    println("Lower location (part 1): ${locations.min()}")
}
