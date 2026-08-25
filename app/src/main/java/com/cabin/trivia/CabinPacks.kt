package com.cabin.trivia

import kotlin.random.Random

object CabinPacks {
    const val SHORT_SIZE = 20

    fun short(catalog: List<Question>, random: Random = Random.Default): List<Question> {
        val n = minOf(SHORT_SIZE, catalog.size)
        return catalog.shuffled(random).take(n)
    }
}
