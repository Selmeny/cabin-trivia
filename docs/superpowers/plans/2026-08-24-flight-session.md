# Flight Session Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** One shuffled run of the bundled catalog, per-answer reveal (mark miss and correct, explanation, Continue), and the same deal restored across rotation — still offline, still `view` as the only public session surface.

**Architecture:** Keep `QuizSession` as the engine. Add `Question.explanation`, `QuizView.Revealing`, `continueAfterReveal()`, constructor shuffle, and a pure `SessionSnapshot` restore path. `MainActivity` stays a binder: `when (session.view)` plus `onSaveInstanceState` / `onCreate`. Mutation indexes the private list; it never downcasts `view`.

**Tech Stack:** Kotlin, AppCompat, XML layouts, JUnit 4, Gradle `./gradlew :app:testDebugUnitTest` / `assembleDebug` (JDK 17, `ANDROID_HOME=/opt/homebrew/share/android-commandlinetools`).

**Spec:** `docs/superpowers/specs/2026-08-24-flight-session-design.md`

**Toolchain (every test/build step):**

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export ANDROID_HOME="/opt/homebrew/share/android-commandlinetools"
cd /Users/selmeny/Projects/cabin-trivia
```

---

## File map

| File | Role |
|---|---|
| `app/src/main/java/com/cabin/trivia/Question.kt` | `explanation` + init |
| `app/src/main/java/com/cabin/trivia/AviationCatalog.kt` | 10 explanations |
| `app/src/main/java/com/cabin/trivia/QuizSession.kt` | `QuizView`, shuffle, reveal/continue, snapshot |
| `app/src/main/java/com/cabin/trivia/MainActivity.kt` | Bind three views; save/restore snapshot |
| `app/src/main/res/layout/activity_main.xml` | progress, explanation, Continue |
| `app/src/main/res/values/strings.xml` | progress, continue |
| `app/src/main/res/values/colors.xml` | right/wrong |
| `app/src/test/java/com/cabin/trivia/QuestionTest.kt` | blank explanation |
| `app/src/test/java/com/cabin/trivia/AviationCatalogTest.kt` | explanations present |
| `app/src/test/java/com/cabin/trivia/QuizSessionTest.kt` | reveal, continue, shuffle seed, snapshot |

Do not add ViewModel, Navigation, Compose, SharedPreferences, or extra catalog items.

---

### Task 1: `explanation` on `Question` and catalog

**Files:**
- Modify: `app/src/main/java/com/cabin/trivia/Question.kt`
- Modify: `app/src/main/java/com/cabin/trivia/AviationCatalog.kt`
- Modify: `app/src/test/java/com/cabin/trivia/QuestionTest.kt`
- Modify: `app/src/test/java/com/cabin/trivia/AviationCatalogTest.kt`
- Modify: `app/src/test/java/com/cabin/trivia/QuizSessionTest.kt` (add `explanation` to fixtures so the project compiles)

- [ ] **Step 1: Extend `QuestionTest` with blank-explanation and valid-explanation cases**

In `QuestionTest.kt`, add `explanation = "Because."` to every existing `Question(...)` so they stay valid, then add:

```kotlin
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
```

Add `import org.junit.Assert.assertEquals`.

- [ ] **Step 2: Run tests — expect compile failure (`explanation` missing on `Question`)**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.QuestionTest
```

Expected: compile error, unresolved parameter `explanation`.

- [ ] **Step 3: Add `explanation` to `Question` and update every constructor**

`Question.kt`:

```kotlin
package com.cabin.trivia

enum class Topic {
    AIRPORT_CODES,
    AIRLINES,
    FAMOUS_FLIGHTS,
    METEOROLOGY
}

data class Question(
    val id: String,
    val topic: Topic,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String
) {
    init {
        require(prompt.isNotBlank()) { "prompt must be non-blank" }
        require(explanation.isNotBlank()) { "explanation must be non-blank" }
        require(choices.size == 4) { "Question must have exactly four choices" }
        require(choices.all { it.isNotBlank() }) { "choices must be non-blank" }
        require(correctIndex in choices.indices) { "correctIndex out of range" }
    }
}
```

Add `explanation = "Because $id."` (or a real sentence) to both items in `QuizSessionTest` fixtures.

In `AviationCatalog.kt`, add a factual 1–2 sentence `explanation` to each of the 10 items:

- `sin`: "Singapore Changi Airport's IATA code is SIN; SGN is Ho Chi Minh City and CGK is Jakarta."
- `ams`: "Amsterdam Schiphol uses AMS. ARN is Stockholm Arlanda; RTM is Rotterdam."
- `jfk`: "JFK is John F. Kennedy International in New York. Los Angeles is LAX; Miami is MIA."
- `qantas`: "Qantas (Australia) paints a kangaroo on the tail. Air New Zealand uses a koru."
- `ana`: "ANA stands for All Nippon Airways, Japan's largest airline by fleet."
- `lufthansa`: "Lufthansa's primary hub is Frankfurt (FRA). Munich (MUC) is the second hub."
- `spirit`: "Charles Lindbergh flew the Spirit of St. Louis from New York to Paris in 1927."
- `af1`: "Air Force One is the call sign for a U.S. Air Force aircraft carrying the President."
- `contrail`: "Contrails are condensation trails from engine exhaust freezing in cold, humid air at altitude."
- `cb`: "Cumulonimbus clouds bring thunderstorms, hail, and severe turbulence. Cirrus are high and thin."

In `AviationCatalogTest`, inside the `forEach`, add:

```kotlin
assertTrue(question.explanation.isNotBlank())
```

- [ ] **Step 4: Run tests**

```bash
./gradlew :app:testDebugUnitTest
```

Expected: `BUILD SUCCESSFUL` (session tests still use the old immediate-advance behavior).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/cabin/trivia/Question.kt \
  app/src/main/java/com/cabin/trivia/AviationCatalog.kt \
  app/src/test/java/com/cabin/trivia/QuestionTest.kt \
  app/src/test/java/com/cabin/trivia/AviationCatalogTest.kt \
  app/src/test/java/com/cabin/trivia/QuizSessionTest.kt
git commit -m "Add required explanation to Question and bundled catalog"
```

---

### Task 2: Reveal + Continue in `QuizSession`

**Files:**
- Modify: `app/src/main/java/com/cabin/trivia/QuizSession.kt`
- Modify: `app/src/test/java/com/cabin/trivia/QuizSessionTest.kt`

This replaces immediate advance. After this task, `answer` enters `Revealing` and does not increment `asked`. Include constructor shuffle with `kotlin.random.Random` now so scoring tests stay order-agnostic (drive via `view`). Snapshot types can wait until Task 4; keep a private `pickedIndex: Int?` for reveal.

- [ ] **Step 1: Rewrite `QuizSessionTest` for reveal/continue**

Replace `QuizSessionTest.kt` with:

```kotlin
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
```

- [ ] **Step 2: Run tests — expect compile/fail (no `Revealing`, no `continueAfterReveal`, no `total`)**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.QuizSessionTest
```

Expected: FAIL / compile errors.

- [ ] **Step 3: Implement session**

Replace `QuizSession.kt` with:

```kotlin
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
        get() = when {
            asked >= items.size -> QuizView.Finished(correct = correct, asked = asked)
            pickedIndex != null -> QuizView.Revealing(
                question = items[asked],
                pickedIndex = pickedIndex!!,
                asked = asked,
                total = items.size
            )
            else -> QuizView.Asking(
                question = items[asked],
                asked = asked,
                total = items.size
            )
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
```

Do **not** use `view as?` in `answer`. The `pickedIndex!!` in `view` is only after `pickedIndex != null` in the same `when`. Prefer a local `val pick = pickedIndex` and smart-cast:

```kotlin
val pick = pickedIndex
when {
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
```

Use that form (no `!!`).

`MainActivity` still compiles if `Asking` gained `total` (unused is fine). `when` on `view` must add `is QuizView.Revealing` or it will not compile. Add a temporary branch that treats Revealing like Asking (choices still enabled) until Task 5 — **or** implement the real reveal UI in Task 5 and for now:

In `MainActivity.render()` add:

```kotlin
is QuizView.Revealing -> {
    val question = view.question
    topicText.visibility = View.VISIBLE
    topicText.text = topicLabel(question.topic)
    promptText.text = question.prompt
    scoreText.visibility = View.GONE
    playAgainButton.visibility = View.GONE
    choiceButtons.forEachIndexed { index, button ->
        button.visibility = View.VISIBLE
        button.text = question.choices[index]
    }
}
```

so the app compiles. Real continue button is Task 5.

- [ ] **Step 4: Run tests**

```bash
./gradlew :app:testDebugUnitTest
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/cabin/trivia/QuizSession.kt \
  app/src/main/java/com/cabin/trivia/MainActivity.kt \
  app/src/test/java/com/cabin/trivia/QuizSessionTest.kt
git commit -m "Enter Revealing on answer; Continue completes the question"
```

---

### Task 3: Shuffle is deterministic by seed

**Files:**
- Modify: `app/src/test/java/com/cabin/trivia/QuizSessionTest.kt`

Constructor already shuffles. Add the spec’s seed test only.

- [ ] **Step 1: Add shuffle test**

```kotlin
@Test
fun sameSeed_sameIdSequence() {
    val seed = 42L
    val a = QuizSession(fixture, random = kotlin.random.Random(seed))
    val b = QuizSession(fixture, random = kotlin.random.Random(seed))
    val idsA = mutableListOf<String>()
    while (a.view is QuizView.Asking) {
        val asking = a.view as QuizView.Asking
        idsA += asking.question.id
        a.answer(0)
        a.continueAfterReveal()
    }
    val idsB = mutableListOf<String>()
    while (b.view is QuizView.Asking) {
        val asking = b.view as QuizView.Asking
        idsB += asking.question.id
        b.answer(0)
        b.continueAfterReveal()
    }
    assertEquals(idsA, idsB)
    assertEquals(fixture.size, idsA.size)
}
```

- [ ] **Step 2: Run test**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.QuizSessionTest.sameSeed_sameIdSequence
```

Expected: PASS (shuffle already implemented). If FAIL, the constructor is not using the passed `Random` in `shuffled(random)`.

- [ ] **Step 3: Commit**

```bash
git add app/src/test/java/com/cabin/trivia/QuizSessionTest.kt
git commit -m "Lock shuffle order to the injected Random seed"
```

---

### Task 4: Snapshot and fail-closed restore

**Files:**
- Modify: `app/src/main/java/com/cabin/trivia/QuizSession.kt`
- Modify: `app/src/test/java/com/cabin/trivia/QuizSessionTest.kt`

- [ ] **Step 1: Add snapshot tests**

```kotlin
@Test
fun snapshotRevealing_roundTripsView() {
    val session = QuizSession(fixture, random = kotlin.random.Random(1))
    val asking = session.view as QuizView.Asking
    session.answer(asking.question.correctIndex)
    val before = session.view
    val restored = QuizSession.restore(session.snapshot(), fixture, kotlin.random.Random(0))
    assertEquals(before, restored.view)
}

@Test
fun snapshotFinished_roundTripsView() {
    val session = QuizSession(fixture, random = kotlin.random.Random(1))
    repeat(fixture.size) {
        val asking = session.view as QuizView.Asking
        session.answer(asking.question.correctIndex)
        session.continueAfterReveal()
    }
    val before = session.view as QuizView.Finished
    val restored = QuizSession.restore(session.snapshot(), fixture, kotlin.random.Random(0))
    assertEquals(before, restored.view)
}

@Test
fun restoreMissingId_startsFreshShuffle() {
    val snapshot = SessionSnapshot(
        ids = listOf("missing", "q1"),
        asked = 0,
        correct = 0,
        phase = Phase.Asking,
        pickedIndex = null
    )
    val restored = QuizSession.restore(snapshot, fixture, kotlin.random.Random(7))
    val view = restored.view
    assertTrue(view is QuizView.Asking)
    val id = (view as QuizView.Asking).question.id
    assertTrue(id == "q1" || id == "q2")
}
```

Add imports if needed (`assertTrue` already present).

- [ ] **Step 2: Run — expect compile failure (`snapshot`, `restore`, `SessionSnapshot`, `Phase` missing)**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.QuizSessionTest
```

- [ ] **Step 3: Implement snapshot types and restore**

Add to `QuizSession.kt` (same file):

```kotlin
enum class Phase { Asking, Revealing, Finished }

data class SessionSnapshot(
    val ids: List<String>,
    val asked: Int,
    val correct: Int,
    val phase: Phase,
    val pickedIndex: Int?
)
```

On `QuizSession`:

```kotlin
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
```

The private constructor already exists from Task 2; `restore` must call it. If the private constructor is not visible from `companion object`, it is — Kotlin companions can call `QuizSession(...)` private constructors.

- [ ] **Step 4: Run tests**

```bash
./gradlew :app:testDebugUnitTest
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/cabin/trivia/QuizSession.kt \
  app/src/test/java/com/cabin/trivia/QuizSessionTest.kt
git commit -m "Restore shuffled deals from SessionSnapshot; fail closed on corrupt ids"
```

---

### Task 5: Activity UI — progress, mark-both, Continue, rotation bundle

**Files:**
- Modify: `app/src/main/res/layout/activity_main.xml`
- Modify: `app/src/main/res/values/strings.xml`
- Modify: `app/src/main/res/values/colors.xml`
- Modify: `app/src/main/java/com/cabin/trivia/MainActivity.kt`

No instrumented tests. Bind the spec’s three screens. Persist snapshot in `onSaveInstanceState`.

- [ ] **Step 1: Resources**

`strings.xml` — add:

```xml
<string name="progress_format">%1$d of %2$d</string>
<string name="continue_button">Continue</string>
```

`colors.xml` — add:

```xml
<color name="answer_correct">#FF2E7D32</color>
<color name="answer_wrong">#FFC62828</color>
```

- [ ] **Step 2: Layout**

In `activity_main.xml`, after `topicText`, add:

```xml
<TextView
    android:id="@+id/progressText"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_marginTop="4dp"
    android:textAppearance="?attr/textAppearanceCaption"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@id/topicText" />
```

Point `promptText` constraint `app:layout_constraintTop_toBottomOf="@id/progressText"`.

After `choice3`, add:

```xml
<TextView
    android:id="@+id/explanationText"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:visibility="gone"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@id/choice3" />

<Button
    android:id="@+id/continueButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:text="@string/continue_button"
    android:visibility="gone"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@id/explanationText" />
```

Point `playAgainButton` `app:layout_constraintTop_toBottomOf="@id/continueButton"`.

- [ ] **Step 3: `MainActivity`**

Replace `MainActivity.kt` with:

```kotlin
package com.cabin.trivia

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var session: QuizSession
    private lateinit var topicText: TextView
    private lateinit var progressText: TextView
    private lateinit var promptText: TextView
    private lateinit var scoreText: TextView
    private lateinit var explanationText: TextView
    private lateinit var continueButton: Button
    private lateinit var playAgainButton: Button
    private lateinit var choiceButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        choiceButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onChoice(index) }
        }
        continueButton.setOnClickListener {
            session.continueAfterReveal()
            render()
        }
        playAgainButton.setOnClickListener { startSession() }

        session = savedInstanceState?.let { bundle ->
            snapshotFrom(bundle)?.let { snap ->
                QuizSession.restore(snap, AviationCatalog.load())
            }
        } ?: QuizSession(AviationCatalog.load())
        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        writeSnapshot(outState, session.snapshot())
    }

    private fun startSession() {
        session = QuizSession(AviationCatalog.load())
        render()
    }

    private fun onChoice(choiceIndex: Int) {
        session.answer(choiceIndex)
        render()
    }

    private fun render() {
        when (val view = session.view) {
            is QuizView.Finished -> {
                topicText.visibility = View.GONE
                progressText.visibility = View.GONE
                promptText.text = getString(R.string.quiz_complete)
                scoreText.visibility = View.VISIBLE
                scoreText.text = getString(R.string.score_format, view.correct, view.asked)
                explanationText.visibility = View.GONE
                continueButton.visibility = View.GONE
                playAgainButton.visibility = View.VISIBLE
                choiceButtons.forEach { button ->
                    button.visibility = View.GONE
                    button.isEnabled = false
                    button.backgroundTintList = null
                }
            }
            is QuizView.Asking -> bindQuestion(view.question, view.asked, view.total, pickedIndex = null)
            is QuizView.Revealing -> bindQuestion(view.question, view.asked, view.total, pickedIndex = view.pickedIndex)
        }
    }

    private fun bindQuestion(
        question: Question,
        asked: Int,
        total: Int,
        pickedIndex: Int?
    ) {
        val revealing = pickedIndex != null
        topicText.visibility = View.VISIBLE
        topicText.text = topicLabel(question.topic)
        progressText.visibility = View.VISIBLE
        progressText.text = getString(R.string.progress_format, asked + 1, total)
        promptText.text = question.prompt
        scoreText.visibility = View.GONE
        playAgainButton.visibility = View.GONE
        if (revealing) {
            explanationText.visibility = View.VISIBLE
            explanationText.text = question.explanation
            continueButton.visibility = View.VISIBLE
        } else {
            explanationText.visibility = View.GONE
            continueButton.visibility = View.GONE
        }
        val correctTint = ContextCompat.getColorStateList(this, R.color.answer_correct)
        val wrongTint = ContextCompat.getColorStateList(this, R.color.answer_wrong)
        choiceButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.text = question.choices[index]
            button.isEnabled = !revealing
            button.backgroundTintList = when {
                pickedIndex == null -> null
                index == question.correctIndex -> correctTint
                index == pickedIndex -> wrongTint
                else -> null
            }
        }
    }

    private fun topicLabel(topic: Topic): String = when (topic) {
        Topic.AIRPORT_CODES -> getString(R.string.topic_airport_codes)
        Topic.AIRLINES -> getString(R.string.topic_airlines)
        Topic.FAMOUS_FLIGHTS -> getString(R.string.topic_famous_flights)
        Topic.METEOROLOGY -> getString(R.string.topic_meteorology)
    }

    private fun writeSnapshot(bundle: Bundle, snapshot: SessionSnapshot) {
        bundle.putStringArrayList(KEY_IDS, ArrayList(snapshot.ids))
        bundle.putInt(KEY_ASKED, snapshot.asked)
        bundle.putInt(KEY_CORRECT, snapshot.correct)
        bundle.putString(KEY_PHASE, snapshot.phase.name)
        if (snapshot.pickedIndex != null) {
            bundle.putInt(KEY_PICKED, snapshot.pickedIndex)
            bundle.putBoolean(KEY_HAS_PICKED, true)
        } else {
            bundle.putBoolean(KEY_HAS_PICKED, false)
        }
    }

    private fun snapshotFrom(bundle: Bundle): SessionSnapshot? {
        val ids = bundle.getStringArrayList(KEY_IDS) ?: return null
        val phaseName = bundle.getString(KEY_PHASE) ?: return null
        val phase = runCatching { Phase.valueOf(phaseName) }.getOrNull() ?: return null
        val picked = if (bundle.getBoolean(KEY_HAS_PICKED, false)) {
            bundle.getInt(KEY_PICKED)
        } else {
            null
        }
        return SessionSnapshot(
            ids = ids,
            asked = bundle.getInt(KEY_ASKED),
            correct = bundle.getInt(KEY_CORRECT),
            phase = phase,
            pickedIndex = picked
        )
    }

    companion object {
        private const val KEY_IDS = "session_ids"
        private const val KEY_ASKED = "session_asked"
        private const val KEY_CORRECT = "session_correct"
        private const val KEY_PHASE = "session_phase"
        private const val KEY_PICKED = "session_picked"
        private const val KEY_HAS_PICKED = "session_has_picked"
    }
}
```

When `pickedIndex == correctIndex`, only the correct button gets `answer_correct` (`index == question.correctIndex` wins first). Wrong extra tint does not apply. Matches spec.

- [ ] **Step 4: Unit tests still pass; assemble debug APK**

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

Expected: `BUILD SUCCESSFUL`. APK at `app/build/outputs/apk/debug/app-debug.apk`.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/cabin/trivia/MainActivity.kt \
  app/src/main/res/layout/activity_main.xml \
  app/src/main/res/values/strings.xml \
  app/src/main/res/values/colors.xml
git commit -m "Bind reveal, progress, and rotation snapshot in MainActivity"
```

---

## Spec coverage

| Spec item | Task |
|---|---|
| `explanation` + catalog text | 1 |
| Revealing, Continue, no-op answer | 2 |
| Shuffle whole catalog / seeded Random | 2 + 3 |
| Empty catalog → Finished(0,0) | 2 |
| Snapshot / fail-closed restore | 4 |
| Activity UI mark-both, progress, Continue | 5 |
| `onSaveInstanceState` | 5 |
| JVM tests table | 1–4 |
| No ViewModel / packs / SharedPreferences | throughout |
| `assembleDebug` | 5 |

## Notes for the implementer

- Do not downcast `view` inside `answer` / `continueAfterReveal`.
- Keep `asked` / `correct` private.
- Play again calls `QuizSession(AviationCatalog.load())` and ignores the old snapshot.
- Current uncommitted quiz-core refactor on `main` is the starting tree; include those files in Task 1’s commit if they are still uncommitted.
