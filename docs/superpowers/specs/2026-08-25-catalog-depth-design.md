# Catalog depth — design

Date: 2026-08-25  
Repo: `cabin-trivia`  
Status: approved in brainstorming; implementation plan not started

## Goal

Grow the bundled aviation catalog from 10 questions to **80–100** so a full shuffle can occupy a short-haul flight. Keep the existing session engine (shuffle all, reveal + Continue, rotation). Update the README so it matches the app.

## Non-goals

- Short vs full packs, 20-minute timers, start lobby
- New `Topic` values
- JSON / download / Play Asset Delivery on-demand
- QuizSession / MainActivity behavior changes (except what falls out of a larger `load()` list)
- Dark-cabin polish, Play listing, ads, IAP, GPS

## Product rules (locked)

- Play / Play again still shuffle **all** of `AviationCatalog.load()`.
- Four topics only: airport codes, airlines, famous flights, meteorology.
- **80–100** questions total; **at least 20 per topic**.
- Keep the existing 10 rows and their ids (`sin`, `ams`, `jfk`, `qantas`, `ana`, `lufthansa`, `spirit`, `af1`, `contrail`, `cb`).
- New ids are unique slugs; never reused.
- Facts must be accurate; explanations 1–2 sentences; four choices; `Question.init` still enforces shape.
- Fully offline: catalog is compiled Kotlin, no network.

## Architecture

Do not put ~100 `Question(...)` blocks in one file (that crosses ~1k lines). Split by topic; `AviationCatalog` only concatenates.

```
AviationCatalog.load()
  = airportCodesQuestions
  + airlinesQuestions
  + famousFlightsQuestions
  + meteorologyQuestions
        → QuizSession (unchanged shuffle)
```

## Files

| File | Role |
|---|---|
| `app/src/main/java/com/cabin/trivia/AirportCodesCatalog.kt` | `internal val airportCodesQuestions: List<Question>` |
| `app/src/main/java/com/cabin/trivia/AirlinesCatalog.kt` | `internal val airlinesQuestions` |
| `app/src/main/java/com/cabin/trivia/FamousFlightsCatalog.kt` | `internal val famousFlightsQuestions` |
| `app/src/main/java/com/cabin/trivia/MeteorologyCatalog.kt` | `internal val meteorologyQuestions` |
| `app/src/main/java/com/cabin/trivia/AviationCatalog.kt` | `load()` concatenates the four lists |
| `app/src/main/java/com/cabin/trivia/Question.kt` | `require(id.isNotBlank())` in `init` |
| `app/src/test/java/com/cabin/trivia/AviationCatalogTest.kt` | size, unique ids, all topics, ≥20 per topic, original 10 ids |
| `app/src/test/java/com/cabin/trivia/QuestionTest.kt` | blank `id` throws |
| `README.md` | play path + pack size |

Existing 10 questions move into the matching topic file (do not duplicate them in `AviationCatalog.kt`).

## `Question` ids

- `init` requires non-blank `id` (in addition to prompt, explanation, four non-blank choices, `correctIndex` in range).
- Uniqueness is a **catalog** invariant: `AviationCatalog.load().map { it.id }.toSet().size == load().size`. A single `Question` cannot know about siblings.

## Tests (JVM, shipped `load()`)

`AviationCatalogTest` must call `AviationCatalog.load()`:

- `size in 80..100`
- all ids unique and non-blank
- `Topic.entries` every value appears at least once
- each topic has `count { it.topic == t } >= 20`
- the ten original ids are present
- each item already satisfies `Question` shape (init); the test may still assert explanation non-blank / four choices as a catalog scan

`QuestionTest`: constructing with `id = "   "` throws `IllegalArgumentException`.

No new session tests required.

## README

Replace the outdated “question + four choices + score” summary. State:

- Offline bundled catalog, no account, no network
- Full shuffle of ~80–100 items across four aviation topics
- Tap answer → mark miss and correct → short explanation → Continue
- End score `You got X of Y`; Play again shuffles again
- Rotation restores the deal (`savedInstanceState`)
- Existing JDK 17 / `ANDROID_HOME` / `./gradlew` toolchain

## Error handling

Invalid rows fail at catalog construction (`Question.init`), not mid-quiz. Duplicate ids fail the catalog test (and must not ship). Session fail-closed restore is unchanged.

## Success

- `./gradlew :app:testDebugUnitTest` passes, including the new catalog assertions
- `./gradlew :app:assembleDebug` succeeds
- Debug APK still has no `INTERNET` permission
- A complete shuffled run is 80–100 reveals long
