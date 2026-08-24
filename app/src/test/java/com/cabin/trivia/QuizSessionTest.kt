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
    fun correctAnswer_entersRevealingWithoutIncrementingAsked() {
        val session = QuizSession(fixture)
        val asking = session.view as QuizView.Asking
        assertEquals(0, asking.asked)
        assertEquals(2, asking.total)

        val result = session.answer(asking.question.correctIndex)

        assertTrue(result)
        val revealing = session.view as QuizView.Revealing
        assertEquals(asking.question, revealing.question)
        assertEquals(asking.question.correctIndex, revealing.pickedIndex)
        assertEquals(0, revealing.asked)
        assertEquals(2, revealing.total)
    }

    @Test
    fun continueAfterCorrect_incrementsScore() {
        val session = QuizSession(fixture)
        val first = session.view as QuizView.Asking
        session.answer(first.question.correctIndex)
        session.continueAfterReveal()

        when (val view = session.view) {
            is QuizView.Asking -> {
                assertEquals(1, view.asked)
                val wrong = (view.question.correctIndex + 1) % view.question.choices.size
                session.answer(wrong)
                session.continueAfterReveal()
            }
            is QuizView.Finished -> {
                assertEquals(1, view.correct)
                assertEquals(1, view.asked)
                return
            }
            is QuizView.Revealing -> error("should have continued")
        }

        val finished = session.view as QuizView.Finished
        assertEquals(1, finished.correct)
        assertEquals(2, finished.asked)
    }

    @Test
    fun incorrectAnswer_thenContinue_doesNotIncrementScore() {
        val session = QuizSession(fixture)
        val first = session.view as QuizView.Asking
        val wrong = (first.question.correctIndex + 1) % first.question.choices.size

        assertFalse(session.answer(wrong))
        val revealing = session.view as QuizView.Revealing
        assertEquals(first.question, revealing.question)
        assertEquals(wrong, revealing.pickedIndex)

        session.continueAfterReveal()

        while (session.view is QuizView.Asking) {
            val asking = session.view as QuizView.Asking
            val miss = (asking.question.correctIndex + 1) % asking.question.choices.size
            session.answer(miss)
            session.continueAfterReveal()
        }

        val finished = session.view as QuizView.Finished
        assertEquals(0, finished.correct)
        assertEquals(fixture.size, finished.asked)
    }

    @Test
    fun answerDuringRevealing_isNoOp() {
        val session = QuizSession(fixture)
        val first = session.view as QuizView.Asking
        session.answer(first.question.correctIndex)
        val before = session.view as QuizView.Revealing

        assertFalse(session.answer(0))
        assertEquals(before, session.view)
    }

    @Test
    fun outOfRangeAnswer_isNoOp() {
        val session = QuizSession(fixture)
        val before = session.view
        assertFalse(session.answer(-1))
        assertFalse(session.answer(4))
        assertEquals(before, session.view)
    }

    @Test
    fun fullRun_reportsScore() {
        val session = QuizSession(fixture)
        var expectedCorrect = 0
        repeat(fixture.size) { index ->
            val asking = session.view as QuizView.Asking
            assertEquals(index, asking.asked)
            val chooseCorrect = index % 2 == 0
            val choice = if (chooseCorrect) {
                asking.question.correctIndex
            } else {
                (asking.question.correctIndex + 1) % asking.question.choices.size
            }
            if (chooseCorrect) expectedCorrect += 1
            session.answer(choice)
            session.continueAfterReveal()
        }
        val finished = session.view as QuizView.Finished
        assertEquals(fixture.size, finished.asked)
        assertEquals(expectedCorrect, finished.correct)
        assertFalse(session.answer(0))
        session.continueAfterReveal()
        assertTrue(session.view is QuizView.Finished)
    }

    @Test
    fun emptyCatalog_isFinished() {
        val session = QuizSession(emptyList())
        assertEquals(QuizView.Finished(correct = 0, asked = 0), session.view)
        assertFalse(session.answer(0))
    }
}
