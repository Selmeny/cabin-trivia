package com.cabin.trivia

sealed class QuizView {
    data class Asking(val question: Question, val asked: Int) : QuizView()
    data class Finished(val correct: Int, val asked: Int) : QuizView()
}

class QuizSession(questions: List<Question>) {
    private val items = questions.toList()
    private var asked: Int = 0
    private var correct: Int = 0

    val view: QuizView
        get() = if (asked >= items.size) {
            QuizView.Finished(correct = correct, asked = asked)
        } else {
            QuizView.Asking(question = items[asked], asked = asked)
        }

    fun answer(choiceIndex: Int): Boolean {
        val question = items.getOrNull(asked) ?: return false
        val isCorrect = choiceIndex == question.correctIndex
        asked += 1
        if (isCorrect) correct += 1
        return isCorrect
    }
}
