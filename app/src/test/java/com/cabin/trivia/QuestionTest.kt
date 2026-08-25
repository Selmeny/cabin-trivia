package com.cabin.trivia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class QuestionTest {

    @Test
    fun invalidShape_throws() {
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "bad-count",
                topic = Topic.AIRPORT_CODES,
                prompt = "Too few choices?",
                choices = listOf("A", "B", "C"),
                correctIndex = 0,
                explanation = "Because."
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "bad-index",
                topic = Topic.AIRLINES,
                prompt = "Index out of range?",
                choices = listOf("A", "B", "C", "D"),
                correctIndex = 4,
                explanation = "Because."
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "blank-prompt",
                topic = Topic.METEOROLOGY,
                prompt = "   ",
                choices = listOf("A", "B", "C", "D"),
                correctIndex = 0,
                explanation = "Because."
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "blank-choice",
                topic = Topic.FAMOUS_FLIGHTS,
                prompt = "Blank choice?",
                choices = listOf("A", " ", "C", "D"),
                correctIndex = 0,
                explanation = "Because."
            )
        }
    }

    @Test
    fun blankExplanation_throws() {
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "blank-exp",
                topic = Topic.AIRPORT_CODES,
                prompt = "Prompt?",
                choices = listOf("A", "B", "C", "D"),
                correctIndex = 0,
                explanation = "   "
            )
        }
    }

    @Test
    fun blankId_throws() {
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "   ",
                topic = Topic.AIRPORT_CODES,
                prompt = "Prompt?",
                choices = listOf("A", "B", "C", "D"),
                correctIndex = 0,
                explanation = "Because."
            )
        }
    }

    @Test
    fun validQuestion_constructs() {
        val q = Question(
            id = "ok",
            topic = Topic.AIRPORT_CODES,
            prompt = "Prompt?",
            choices = listOf("A", "B", "C", "D"),
            correctIndex = 0,
            explanation = "The IATA code is A."
        )
        assertEquals("The IATA code is A.", q.explanation)
    }
}
