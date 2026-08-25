package com.cabin.trivia

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var session: QuizSession? = null
    private lateinit var startTitle: TextView
    private lateinit var startShortButton: Button
    private lateinit var startFullButton: Button
    private lateinit var topicText: TextView
    private lateinit var progressText: TextView
    private lateinit var promptText: TextView
    private lateinit var scoreText: TextView
    private lateinit var explanationText: TextView
    private lateinit var continueButton: Button
    private lateinit var playAgainButton: Button
    private lateinit var choiceButtons: List<Button>
    private lateinit var defaultChoiceTint: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTitle = findViewById(R.id.startTitle)
        startShortButton = findViewById(R.id.startShortButton)
        startFullButton = findViewById(R.id.startFullButton)
        topicText = findViewById(R.id.topicText)
        progressText = findViewById(R.id.progressText)
        promptText = findViewById(R.id.promptText)
        scoreText = findViewById(R.id.scoreText)
        explanationText = findViewById(R.id.explanationText)
        continueButton = findViewById(R.id.continueButton)
        playAgainButton = findViewById(R.id.playAgainButton)
        choiceButtons = listOf(
            findViewById(R.id.choice0),
            findViewById(R.id.choice1),
            findViewById(R.id.choice2),
            findViewById(R.id.choice3)
        )
        defaultChoiceTint = requireNotNull(startShortButton.backgroundTintList)

        startShortButton.setOnClickListener {
            session = QuizSession(CabinPacks.short(AviationCatalog.load()))
            render()
        }
        startFullButton.setOnClickListener {
            session = QuizSession(AviationCatalog.load())
            render()
        }
        choiceButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onChoice(index) }
        }
        continueButton.setOnClickListener {
            session?.continueAfterReveal()
            render()
        }
        playAgainButton.setOnClickListener {
            session = null
            render()
        }

        session = savedInstanceState?.let { bundle ->
            snapshotFrom(bundle)?.let { snap ->
                QuizSession.restore(snap, AviationCatalog.load())
            }
        }
        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        session?.let { writeSnapshot(outState, it.snapshot()) }
    }

    private fun onChoice(choiceIndex: Int) {
        session?.answer(choiceIndex)
        render()
    }

    private fun render() {
        val current = session
        if (current == null) {
            showStart()
            return
        }
        hideStart()
        when (val view = current.view) {
            is QuizView.Finished -> {
                topicText.visibility = View.GONE
                progressText.visibility = View.GONE
                promptText.visibility = View.VISIBLE
                promptText.text = getString(R.string.quiz_complete)
                scoreText.visibility = View.VISIBLE
                scoreText.text = getString(R.string.score_format, view.correct, view.asked)
                explanationText.visibility = View.GONE
                continueButton.visibility = View.GONE
                playAgainButton.visibility = View.VISIBLE
                choiceButtons.forEach { button ->
                    button.visibility = View.GONE
                    button.isEnabled = false
                    button.backgroundTintList = defaultChoiceTint
                }
            }
            is QuizView.Asking -> bindAsking(view)
            is QuizView.Revealing -> bindRevealing(view)
        }
    }

    private fun showStart() {
        startTitle.visibility = View.VISIBLE
        startShortButton.visibility = View.VISIBLE
        startFullButton.visibility = View.VISIBLE
        topicText.visibility = View.GONE
        progressText.visibility = View.GONE
        promptText.visibility = View.GONE
        scoreText.visibility = View.GONE
        explanationText.visibility = View.GONE
        continueButton.visibility = View.GONE
        playAgainButton.visibility = View.GONE
        choiceButtons.forEach { it.visibility = View.GONE }
    }

    private fun hideStart() {
        startTitle.visibility = View.GONE
        startShortButton.visibility = View.GONE
        startFullButton.visibility = View.GONE
    }

    private fun bindAsking(view: QuizView.Asking) {
        bindQuestionChrome(view.question, view.asked, view.total)
        explanationText.visibility = View.GONE
        continueButton.visibility = View.GONE
        choiceButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.text = view.question.choices[index]
            button.isEnabled = true
            button.backgroundTintList = defaultChoiceTint
        }
    }

    private fun bindRevealing(view: QuizView.Revealing) {
        bindQuestionChrome(view.question, view.asked, view.total)
        explanationText.visibility = View.VISIBLE
        explanationText.text = view.question.explanation
        continueButton.visibility = View.VISIBLE
        val correctTint = ContextCompat.getColorStateList(this, R.color.answer_correct)
        val wrongTint = ContextCompat.getColorStateList(this, R.color.answer_wrong)
        choiceButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.text = view.question.choices[index]
            button.isEnabled = false
            button.backgroundTintList = when {
                index == view.question.correctIndex -> correctTint
                index == view.pickedIndex -> wrongTint
                else -> defaultChoiceTint
            }
        }
    }

    private fun bindQuestionChrome(question: Question, asked: Int, total: Int) {
        topicText.visibility = View.VISIBLE
        topicText.text = topicLabel(question.topic)
        progressText.visibility = View.VISIBLE
        progressText.text = getString(R.string.progress_format, asked + 1, total)
        promptText.visibility = View.VISIBLE
        promptText.text = question.prompt
        scoreText.visibility = View.GONE
        playAgainButton.visibility = View.GONE
    }

    private fun topicLabel(topic: Topic): String = when (topic) {
        Topic.AIRPORT_CODES -> getString(R.string.topic_airport_codes)
        Topic.AIRLINES -> getString(R.string.topic_airlines)
        Topic.FAMOUS_FLIGHTS -> getString(R.string.topic_famous_flights)
        Topic.METEOROLOGY -> getString(R.string.topic_meteorology)
    }

    private fun writeSnapshot(bundle: Bundle, snapshot: SessionSnapshot) {
        bundle.putStringArrayList(KEY_IDS, ArrayList(snapshot.questions.map { it.id }))
        bundle.putInt(KEY_ASKED, snapshot.asked)
        bundle.putInt(KEY_CORRECT, snapshot.correct)
        bundle.putInt(KEY_SIZE, snapshot.questions.size)
        if (snapshot.pickedIndex != null) {
            bundle.putInt(KEY_PICKED, snapshot.pickedIndex)
            bundle.putBoolean(KEY_HAS_PICKED, true)
        } else {
            bundle.putBoolean(KEY_HAS_PICKED, false)
        }
        snapshot.questions.forEachIndexed { index, question ->
            bundle.putStringArrayList(KEY_CHOICES_PREFIX + index, ArrayList(question.choices))
            bundle.putInt(KEY_CORRECT_PREFIX + index, question.correctIndex)
        }
    }

    private fun snapshotFrom(bundle: Bundle): SessionSnapshot? {
        val ids = bundle.getStringArrayList(KEY_IDS) ?: return null
        val size = bundle.getInt(KEY_SIZE, ids.size)
        if (size != ids.size) return null
        val questions = ArrayList<DealtQuestion>(size)
        for (index in 0 until size) {
            val choices = bundle.getStringArrayList(KEY_CHOICES_PREFIX + index) ?: return null
            questions.add(
                DealtQuestion(
                    id = ids[index],
                    choices = choices,
                    correctIndex = bundle.getInt(KEY_CORRECT_PREFIX + index)
                )
            )
        }
        val picked = if (bundle.getBoolean(KEY_HAS_PICKED, false)) {
            bundle.getInt(KEY_PICKED)
        } else {
            null
        }
        return SessionSnapshot(
            questions = questions,
            asked = bundle.getInt(KEY_ASKED),
            correct = bundle.getInt(KEY_CORRECT),
            pickedIndex = picked
        )
    }

    companion object {
        private const val KEY_IDS = "session_ids"
        private const val KEY_ASKED = "session_asked"
        private const val KEY_CORRECT = "session_correct"
        private const val KEY_PICKED = "session_picked"
        private const val KEY_HAS_PICKED = "session_has_picked"
        private const val KEY_SIZE = "session_size"
        private const val KEY_CHOICES_PREFIX = "session_choices_"
        private const val KEY_CORRECT_PREFIX = "session_correct_at_"
    }
}
