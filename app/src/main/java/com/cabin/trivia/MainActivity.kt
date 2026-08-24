package com.cabin.trivia

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var session: QuizSession
    private lateinit var topicText: TextView
    private lateinit var promptText: TextView
    private lateinit var scoreText: TextView
    private lateinit var playAgainButton: Button
    private lateinit var choiceButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topicText = findViewById(R.id.topicText)
        promptText = findViewById(R.id.promptText)
        scoreText = findViewById(R.id.scoreText)
        playAgainButton = findViewById(R.id.playAgainButton)
        choiceButtons = listOf(
            findViewById(R.id.choice0),
            findViewById(R.id.choice1),
            findViewById(R.id.choice2),
            findViewById(R.id.choice3)
        )

        choiceButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onChoice(index) }
        }
        playAgainButton.setOnClickListener { startSession() }

        startSession()
    }

    private fun startSession() {
        session = QuizSession(AviationCatalog.load())
        render()
    }

    private fun onChoice(choiceIndex: Int) {
        if (session.isComplete) return
        session.answer(choiceIndex)
        render()
    }

    private fun render() {
        if (session.isComplete) {
            topicText.visibility = View.GONE
            promptText.text = getString(R.string.quiz_complete)
            scoreText.visibility = View.VISIBLE
            scoreText.text = getString(R.string.score_format, session.correct, session.asked)
            playAgainButton.visibility = View.VISIBLE
            choiceButtons.forEach { it.visibility = View.GONE }
            return
        }

        val question = session.current ?: return
        topicText.visibility = View.VISIBLE
        topicText.text = topicLabel(question.topic)
        promptText.text = question.prompt
        scoreText.visibility = View.GONE
        playAgainButton.visibility = View.GONE
        choiceButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.text = question.choices.getOrElse(index) { "" }
        }
    }

    private fun topicLabel(topic: String): String = when (topic) {
        AviationCatalog.TOPIC_AIRPORT_CODES -> getString(R.string.topic_airport_codes)
        AviationCatalog.TOPIC_AIRLINES -> getString(R.string.topic_airlines)
        AviationCatalog.TOPIC_FAMOUS_FLIGHTS -> getString(R.string.topic_famous_flights)
        AviationCatalog.TOPIC_METEOROLOGY -> getString(R.string.topic_meteorology)
        else -> topic
    }
}
