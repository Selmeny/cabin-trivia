# Flight session — design

Date: 2026-08-24  
Repo: `cabin-trivia`  
Status: approved in brainstorming; implementation plan not started

## Goal

Turn the existing offline quiz core into a real **flight session**: one shuffled run of the full bundled catalog, per-answer reveal (mark miss and correct, short explanation, Continue), and the same deal restored across rotation. Still free, still airplane-mode, still no account.

## Non-goals

- Short vs full packs, 20-minute timed modes, or a start lobby
- Catalog growth beyond adding `explanation` to the existing 10 questions
- Dark-cabin polish, one-handed/landscape as a gating bar, Compose, Navigation, ViewModel
- Play listing, ads, IAP, GPS, live daily packs
- Disk persistence / SharedPreferences (rotation uses `savedInstanceState` only)

Those stay later slices (catalog depth, cabin UI, ship).

## Product rules (locked)

- Every Play / Play again shuffles **all** bundled questions.
- Tap a choice → **Revealing**: wrong pick marked wrong, correct choice marked right (if the pick is correct, only that button is marked right). Short explanation. Continue.
- Continue completes the question (increments `asked`, and `correct` if the pick matched), then next Asking or Finished.
- Score remains correct / questions completed.
- Rotation restores this deal, not a new shuffle.

## Architecture

`QuizSession` is the only engine. `view` is the only public read model. Mutation indexes the private list; it never downcasts `view`. `MainActivity` binds `when (session.view)` and writes/reads a session snapshot in `onSaveInstanceState` / `onCreate`.

```
AviationCatalog.load()
        → QuizSession (copy + shuffle)
                → view: Asking | Revealing | Finished
MainActivity.render() binds view
answer(i) / continueAfterReveal() mutate session
```

## Question

Add required `explanation: String`. `init` requires it non-blank, same as `prompt`. Keep: `id`, `topic: Topic`, `prompt`, four non-blank `choices`, `correctIndex` in range.

All 10 catalog items get a 1–2 sentence factual explanation. No new topics.

## QuizView

```kotlin
sealed class QuizView {
    data class Asking(
        val question: Question,
        val asked: Int,   // completed so far; 0 on the first question
        val total: Int
    ) : QuizView()

    data class Revealing(
        val question: Question,
        val pickedIndex: Int,
        val asked: Int,   // still the completed count; not incremented yet
        val total: Int
    ) : QuizView()

    data class Finished(
        val correct: Int,
        val asked: Int    // == total after the last Continue
    ) : QuizView()
}
```

UI progress on Asking/Revealing is `asked + 1 of total` (the question number on screen).

## Session API

Constructor: `QuizSession(questions: List<Question>, random: Random = Random.Default)`. Copies the list, shuffles once with `random`. Production uses the default. Scoring tests do not depend on fixture order: they read `Asking.question` from `view` and tap `correctIndex` / a wrong index. Shuffle tests pass a seeded `Random` and assert the **same** seed yields the same id sequence. Do not assert that two different seeds differ (can flake for small catalogs).

Empty list → `Finished(0, 0)`.

| Call | From | Effect |
|---|---|---|
| `answer(i)` | Asking, `i` in `0..3` | → Revealing with that `pickedIndex`. Does not change `asked`/`correct`. Returns whether `i == correctIndex`. |
| `answer(i)` | otherwise, or `i` out of range | no-op, `false` |
| `continueAfterReveal()` | Revealing | `asked += 1`; if pick was correct, `correct += 1`; then Asking or Finished |
| `continueAfterReveal()` | otherwise | no-op |
| Play again | Activity | `QuizSession(AviationCatalog.load())` — new shuffle, ignore snapshot |

## Rotation snapshot

Pure Kotlin (no Android types). Activity stores it in the instance `Bundle`.

```kotlin
data class SessionSnapshot(
    val ids: List<String>,
    val asked: Int,
    val correct: Int,
    val phase: Phase,           // Asking, Revealing, Finished
    val pickedIndex: Int?       // non-null iff Revealing
)

enum class Phase { Asking, Revealing, Finished }
```

Restore: map `ids` through `AviationCatalog.load()` keyed by `id`, preserving snapshot order. Then apply `asked`, `correct`, `phase`, `pickedIndex`.

**Fail closed** (fresh `QuizSession(AviationCatalog.load())`, new shuffle) if:

- any id is missing from the catalog
- `asked` not in `0..ids.size` (Finished requires `asked == ids.size`)
- `correct` not in `0..asked`
- `phase == Revealing` and (`pickedIndex` is null or not in `0..3`, or `asked >= ids.size`)
- `phase == Asking` and `asked >= ids.size` (should have been Finished)
- `phase == Finished` and `asked != ids.size`
- empty `ids` (treat as empty finished only if catalog is also empty; otherwise fresh shuffle)

`onCreate`: restore if the bundle has a snapshot that validates; else new session. Play again does not restore.

## UI

One `AppCompatActivity`, one XML layout, current navy theme. No ViewModel / Navigation / Compose.

**Asking:** topic, prompt, progress (`asked + 1 of total`), four enabled choices. Explanation and Continue gone.

**Revealing:** same topic/prompt/progress. Choices visible, not tappable. Correct pick: that button marked right. Wrong pick: tapped button marked wrong **and** `correctIndex` marked right. Explanation visible. Continue visible → `continueAfterReveal()` then `render()`. Choice taps call `answer` which is a no-op.

**Finished:** hide topic, progress, choices, explanation, Continue. Show `You got X of Y` and Play again.

Right/wrong is a background or text color on the existing buttons, not a new screen. Add `progressText`, `explanationText`, `continueButton`.

## Tests (JVM, shipped types)

- `Question`: blank explanation throws; valid shape still constructs.
- `AviationCatalog`: >1 question, ≥2 topics, every item has non-blank explanation (and existing invariants).
- Session with a two-item fixture (order-agnostic; drive via `view`):
  - `answer(correctIndex)` → Revealing, same question, `asked` unchanged.
  - Continue after correct → Finished.correct increased (or next Asking if more remain).
  - Wrong pick → Revealing still that question; Continue does not increase Finished.correct.
  - `answer` during Revealing is a no-op.
  - N answers + N continues → Finished with expected score.
- Shuffle: size > 1, same seed → same id sequence.
- Snapshot at Revealing and at Finished round-trips to an equal `view`.
- Restore with a missing id does not yield the corrupt deal (fresh session).

No instrumented UI tests in this slice.

## Error handling

No dialogs. Invalid calls are no-ops. Bad snapshots become a new shuffle. `Question` `init` still throws on invalid catalog at load (fail at start, not mid-flight).

## Files expected to change

- `Question.kt` — `explanation`
- `AviationCatalog.kt` — 10 explanations
- `QuizSession.kt` — shuffle, Revealing, continue, snapshot/restore
- `MainActivity.kt` + `activity_main.xml` + `strings.xml` / colors as needed
- `QuizSessionTest.kt`, `AviationCatalogTest.kt`, `QuestionTest.kt`

## Success

A debug APK still builds. A complete shuffled session (answer → reveal → continue, all questions) reports score with no network. Rotation mid-reveal shows the same question, pick, and explanation. JVM tests cover the table above.
