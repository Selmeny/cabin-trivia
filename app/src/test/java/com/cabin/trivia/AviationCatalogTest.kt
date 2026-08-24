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
        assertTrue(
            "catalog must span more than one aviation topic, found $topics",
            topics.size >= 2
        )
        catalog.forEach { question ->
            assertTrue(question.prompt.isNotBlank())
            assertTrue(question.explanation.isNotBlank())
            assertEquals(4, question.choices.size)
            assertTrue(question.choices.all { it.isNotBlank() })
            assertTrue(question.correctIndex in question.choices.indices)
        }
    }
}
