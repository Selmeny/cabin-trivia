package com.cabin.trivia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AviationCatalogTest {

    private val originalIds = listOf(
        "sin", "ams", "jfk", "qantas", "ana", "lufthansa",
        "spirit", "af1", "contrail", "cb"
    )

    @Test
    fun load_meetsDepthAndInvariants() {
        val catalog = AviationCatalog.load()

        assertTrue("size must be 80..100, was ${catalog.size}", catalog.size in 80..100)
        val ids = catalog.map { it.id }
        assertTrue(ids.all { it.isNotBlank() })
        assertEquals("ids must be unique", ids.size, ids.toSet().size)
        assertEquals(Topic.entries.toSet(), catalog.map { it.topic }.toSet())
        Topic.entries.forEach { topic ->
            val n = catalog.count { it.topic == topic }
            assertTrue("$topic must have at least 20 questions, was $n", n >= 20)
        }
        val idSet = ids.toSet()
        originalIds.forEach { id ->
            assertTrue("missing original id $id", id in idSet)
        }
        catalog.forEach { question ->
            assertTrue(question.prompt.isNotBlank())
            assertTrue(question.explanation.isNotBlank())
            assertEquals(4, question.choices.size)
            assertTrue(question.choices.all { it.isNotBlank() })
            assertTrue(question.correctIndex in question.choices.indices)
        }
    }
}
