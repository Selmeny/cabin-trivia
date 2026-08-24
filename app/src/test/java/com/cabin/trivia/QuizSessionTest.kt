package com.cabin.trivia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizSessionTest {

    @Test
    fun correctAnswer_incrementsScore() {
        val catalog = AviationCatalog.load()
        val session = QuizSession(catalog)
        val question = session.current!!

        val result = session.answer(question.correctIndex)

        assertTrue(result)
        assertEquals(1, session.asked)
        assertEquals(1, session.correct)
    }

    @Test
    fun incorrectAnswer_doesNotIncrementScore() {
        val catalog = AviationCatalog.load()
        val session = QuizSession(catalog)
        val question = session.current!!
        val wrong = (question.correctIndex + 1) % question.choices.size

        val result = session.answer(wrong)

        assertFalse(result)
        assertEquals(1, session.asked)
        assertEquals(0, session.correct)
    }

    @Test
    fun multiQuestionSession_reportsCorrectCountAndQuestionsAsked() {
        val catalog = AviationCatalog.load()
        assertTrue(catalog.size > 1)
        val session = QuizSession(catalog)

        var expectedCorrect = 0
        catalog.forEachIndexed { index, question ->
            val chooseCorrect = index % 2 == 0
            val choice = if (chooseCorrect) question.correctIndex else (question.correctIndex + 1) % question.choices.size
            if (chooseCorrect) expectedCorrect += 1
            session.answer(choice)
        }

        assertTrue(session.isComplete)
        assertNull(session.current)
        assertEquals(catalog.size, session.asked)
        assertEquals(expectedCorrect, session.correct)
        assertFalse(session.answer(0))
    }
}
