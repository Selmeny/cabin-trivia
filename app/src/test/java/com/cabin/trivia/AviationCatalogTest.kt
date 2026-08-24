package com.cabin.trivia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AviationCatalogTest {

    @Test
    fun load_returnsBundledAviationQuestionsAcrossTopics() {
        val catalog = AviationCatalog.load()

        assertTrue("catalog must contain more than one question", catalog.size > 1)
        val topics = catalog.map { it.topic }.toSet()
        val required = setOf(
            AviationCatalog.TOPIC_AIRPORT_CODES,
            AviationCatalog.TOPIC_AIRLINES,
            AviationCatalog.TOPIC_FAMOUS_FLIGHTS,
            AviationCatalog.TOPIC_METEOROLOGY
        )
        assertTrue(
            "catalog must span more than one aviation topic, found $topics",
            topics.intersect(required).size >= 2
        )
        catalog.forEach { question ->
            assertEquals(4, question.choices.size)
            assertTrue(question.correctIndex in question.choices.indices)
        }
    }
}
