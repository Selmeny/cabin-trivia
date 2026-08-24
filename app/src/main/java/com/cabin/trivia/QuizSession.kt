package com.cabin.trivia

import kotlin.random.Random

enum class Phase { Asking, Revealing, Finished }

data class SessionSnapshot(
    val ids: List<String>,
    val asked: Int,
    val correct: Int,
    val phase: Phase,
    val pickedIndex: Int?
)

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

    fun snapshot(): SessionSnapshot {
        val phase = when {
            asked >= items.size -> Phase.Finished
            pickedIndex != null -> Phase.Revealing
            else -> Phase.Asking
        }
        return SessionSnapshot(
            ids = items.map { it.id },
            asked = asked,
            correct = correct,
            phase = phase,
            pickedIndex = pickedIndex
        )
    }

    companion object {
        fun restore(
            snapshot: SessionSnapshot,
            catalog: List<Question>,
            random: Random = Random.Default
        ): QuizSession {
            if (!isValid(snapshot, catalog)) {
                return QuizSession(catalog, random)
            }
            val byId = catalog.associateBy { it.id }
            val items = snapshot.ids.map { id -> byId.getValue(id) }
            val pick = if (snapshot.phase == Phase.Revealing) snapshot.pickedIndex else null
            return QuizSession(
                items = items,
                asked = snapshot.asked,
                correct = snapshot.correct,
                pickedIndex = pick
            )
        }

        private fun isValid(snapshot: SessionSnapshot, catalog: List<Question>): Boolean {
            val size = snapshot.ids.size
            if (snapshot.ids.isEmpty()) {
                return catalog.isEmpty()
            }
            val catalogIds = catalog.map { it.id }.toSet()
            if (snapshot.ids.any { it !in catalogIds }) return false
            if (snapshot.asked !in 0..size) return false
            if (snapshot.correct !in 0..snapshot.asked) return false
            return when (snapshot.phase) {
                Phase.Finished -> snapshot.asked == size && snapshot.pickedIndex == null
                Phase.Asking -> snapshot.asked < size && snapshot.pickedIndex == null
                Phase.Revealing -> {
                    val pick = snapshot.pickedIndex
                    snapshot.asked < size && pick != null && pick in 0..3
                }
            }
        }
    }
}
