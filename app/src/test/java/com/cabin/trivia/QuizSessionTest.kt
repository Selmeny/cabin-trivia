package com.cabin.trivia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizSessionTest {

    private val fixture = listOf(
        Question(
            id = "q1",
            topic = Topic.AIRPORT_CODES,
            prompt = "First question?",
            choices = listOf("A", "B", "C", "D"),
            correctIndex = 0,
            explanation = "Because q1."
        ),
        Question(
            id = "q2",
            topic = Topic.AIRLINES,
            prompt = "Second question?",
            choices = listOf("W", "X", "Y", "Z"),
            correctIndex = 2,
            explanation = "Because q2."
        )
    )

    @Test
    fun correctAnswer_incrementsScore() {
        val session = QuizSession(fixture)
        val question = (session.view as QuizView.Asking).question

        val result = session.answer(question.correctIndex)

        assertTrue(result)
        val remaining = session.view as QuizView.Asking
        assertEquals(fixture[1], remaining.question)
        assertEquals(1, remaining.asked)

        session.answer((remaining.question.correctIndex + 1) % remaining.question.choices.size)
        val finished = session.view as QuizView.Finished
        assertEquals(1, finished.correct)
        assertEquals(fixture.size, finished.asked)
    }

    @Test
    fun incorrectAnswer_doesNotIncrementScore() {
        val session = QuizSession(fixture)
        val question = (session.view as QuizView.Asking).question
        val wrong = (question.correctIndex + 1) % question.choices.size

        val result = session.answer(wrong)

        assertFalse(result)
        val remaining = session.view as QuizView.Asking
        assertEquals(fixture[1], remaining.question)
        assertEquals(1, remaining.asked)

        session.answer((remaining.question.correctIndex + 1) % remaining.question.choices.size)
        val finished = session.view as QuizView.Finished
        assertEquals(0, finished.correct)
        assertEquals(fixture.size, finished.asked)
    }

    @Test
    fun multiQuestionSession_reportsCorrectCountAndQuestionsAsked() {
        val session = QuizSession(fixture)

        var expectedCorrect = 0
        fixture.forEachIndexed { index, question ->
            val asking = session.view as QuizView.Asking
            assertEquals(question, asking.question)
            assertEquals(index, asking.asked)
            val chooseCorrect = index % 2 == 0
            val choice = if (chooseCorrect) question.correctIndex else (question.correctIndex + 1) % question.choices.size
            if (chooseCorrect) expectedCorrect += 1
            session.answer(choice)
        }

        val finished = session.view as QuizView.Finished
        assertEquals(fixture.size, finished.asked)
        assertEquals(expectedCorrect, finished.correct)
        assertFalse(session.answer(0))
        assertTrue(session.view is QuizView.Finished)
    }
}
