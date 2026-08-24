package com.cabin.trivia

import kotlin.random.Random

sealed class QuizView {
    data class Asking(
        val question: Question,
        val asked: Int,
        val total: Int
    ) : QuizView()

    data class Revealing(
        val question: Question,
        val pickedIndex: Int,
        val asked: Int,
        val total: Int
    ) : QuizView()

    data class Finished(
        val correct: Int,
        val asked: Int
    ) : QuizView()
}

class QuizSession private constructor(
    private val items: List<Question>,
    private var asked: Int,
    private var correct: Int,
    private var pickedIndex: Int?
) {
    constructor(
        questions: List<Question>,
        random: Random = Random.Default
    ) : this(
        items = questions.toList().shuffled(random),
        asked = 0,
        correct = 0,
        pickedIndex = null
    )

    val view: QuizView
        get() {
            val pick = pickedIndex
            return when {
                asked >= items.size -> QuizView.Finished(correct = correct, asked = asked)
                pick != null -> QuizView.Revealing(
                    question = items[asked],
                    pickedIndex = pick,
                    asked = asked,
                    total = items.size
                )
                else -> QuizView.Asking(
                    question = items[asked],
                    asked = asked,
                    total = items.size
                )
            }
        }

    fun answer(choiceIndex: Int): Boolean {
        if (pickedIndex != null) return false
        val question = items.getOrNull(asked) ?: return false
        if (choiceIndex !in question.choices.indices) return false
        pickedIndex = choiceIndex
        return choiceIndex == question.correctIndex
    }

    fun continueAfterReveal() {
        val pick = pickedIndex ?: return
        val question = items.getOrNull(asked) ?: return
        asked += 1
        if (pick == question.correctIndex) {
            correct += 1
        }
        pickedIndex = null
    }
}
