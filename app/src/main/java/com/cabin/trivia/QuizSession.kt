package com.cabin.trivia

class QuizSession(questions: List<Question>) {
    private val items = questions.toList()
    private var index = 0

    var asked: Int = 0
        private set

    var correct: Int = 0
        private set

    val isComplete: Boolean
        get() = index >= items.size

    val current: Question?
        get() = items.getOrNull(index)

    fun answer(choiceIndex: Int): Boolean {
        val question = current ?: return false
        asked += 1
        val isCorrect = choiceIndex == question.correctIndex
        if (isCorrect) {
            correct += 1
        }
        index += 1
        return isCorrect
    }
}
