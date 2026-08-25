# Catalog Depth Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Grow the bundled catalog to 80–100 factual aviation questions (four topics, ≥20 each), split by file so none hits 1k lines, and update the README to match shuffle/reveal play.

**Architecture:** Move the existing 10 items into four `internal val` lists (`AirportCodesCatalog.kt`, `AirlinesCatalog.kt`, `FamousFlightsCatalog.kt`, `MeteorologyCatalog.kt`). `AviationCatalog.load()` concatenates. `Question.init` requires a non-blank `id`. Uniqueness is asserted on `load()`. No QuizSession/MainActivity behavior changes. No JSON.

**Tech Stack:** Kotlin, JUnit 4, Gradle. Toolchain:

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export ANDROID_HOME="/opt/homebrew/share/android-commandlinetools"
cd /Users/selmeny/Projects/cabin-trivia
```

**Spec:** `docs/superpowers/specs/2026-08-25-catalog-depth-design.md`

---

## File map

| File | Role |
|---|---|
| `app/src/main/java/com/cabin/trivia/Question.kt` | `require(id.isNotBlank())` |
| `app/src/main/java/com/cabin/trivia/AirportCodesCatalog.kt` | 23 airport-code questions |
| `app/src/main/java/com/cabin/trivia/AirlinesCatalog.kt` | 23 airline questions |
| `app/src/main/java/com/cabin/trivia/FamousFlightsCatalog.kt` | 22 famous-flight questions |
| `app/src/main/java/com/cabin/trivia/MeteorologyCatalog.kt` | 22 meteorology questions |
| `app/src/main/java/com/cabin/trivia/AviationCatalog.kt` | `load()` = four lists concatenated (90 items) |
| `app/src/test/java/com/cabin/trivia/QuestionTest.kt` | blank id |
| `app/src/test/java/com/cabin/trivia/AviationCatalogTest.kt` | size 80–100, unique ids, ≥20/topic, original 10 ids |
| `README.md` | play path + pack size |

Do not modify `QuizSession.kt` or `MainActivity.kt`.

---

### Task 1: Non-blank `id` on `Question`

**Files:**
- Modify: `app/src/main/java/com/cabin/trivia/Question.kt`
- Modify: `app/src/test/java/com/cabin/trivia/QuestionTest.kt`

- [ ] **Step 1: Add failing test**

In `QuestionTest.kt`, add:

```kotlin
@Test
fun blankId_throws() {
    assertThrows(IllegalArgumentException::class.java) {
        Question(
            id = "   ",
            topic = Topic.AIRPORT_CODES,
            prompt = "Prompt?",
            choices = listOf("A", "B", "C", "D"),
            correctIndex = 0,
            explanation = "Because."
        )
    }
}
```

- [ ] **Step 2: Run — expect FAIL (blank id currently allowed)**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.QuestionTest.blankId_throws
```

Expected: FAIL (test runs and does not throw, or assertion fails).

- [ ] **Step 3: Add to `Question` `init` (keep existing requires)**

```kotlin
require(id.isNotBlank()) { "id must be non-blank" }
```

Place it with the other `require`s.

- [ ] **Step 4: Run tests**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.QuestionTest
./gradlew :app:testDebugUnitTest
```

Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/cabin/trivia/Question.kt \
  app/src/test/java/com/cabin/trivia/QuestionTest.kt
git commit -m "Reject blank Question ids at construction"
```

---

### Task 2: Split the existing 10 questions into topic files

**Files:**
- Create: the four catalog files with **only the current 10 items** (3+3+2+2)
- Modify: `AviationCatalog.kt` to concatenate

This keeps the project compiling after the split, before the bulk add.

- [ ] **Step 1: Create topic files with the existing items only**

`AirportCodesCatalog.kt` — existing `sin`, `ams`, `jfk` (copy verbatim from current `AviationCatalog.kt`).

`AirlinesCatalog.kt` — `qantas`, `ana`, `lufthansa`.

`FamousFlightsCatalog.kt` — `spirit`, `af1`.

`MeteorologyCatalog.kt` — `contrail`, `cb`.

Each file:

```kotlin
package com.cabin.trivia

internal val airportCodesQuestions: List<Question> = listOf(
    // existing three Question(...) blocks, unchanged
)
```

Use `airlinesQuestions`, `famousFlightsQuestions`, `meteorologyQuestions` for the other files.

- [ ] **Step 2: Replace `AviationCatalog.kt` with**

```kotlin
package com.cabin.trivia

object AviationCatalog {
    fun load(): List<Question> =
        airportCodesQuestions +
            airlinesQuestions +
            famousFlightsQuestions +
            meteorologyQuestions
}
```

- [ ] **Step 3: Run tests (still size > 1, four topics present)**

```bash
./gradlew :app:testDebugUnitTest
```

Expected: BUILD SUCCESSFUL. Do not yet require size 80–100.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/cabin/trivia/AviationCatalog.kt \
  app/src/main/java/com/cabin/trivia/AirportCodesCatalog.kt \
  app/src/main/java/com/cabin/trivia/AirlinesCatalog.kt \
  app/src/main/java/com/cabin/trivia/FamousFlightsCatalog.kt \
  app/src/main/java/com/cabin/trivia/MeteorologyCatalog.kt
git commit -m "Split bundled catalog into four topic files"
```

---

### Task 3: Catalog tests for depth, then fill to 90 items

**Files:**
- Modify: `AviationCatalogTest.kt`
- Modify: the four topic catalog files (append new `Question`s; do not remove the original 10 ids)

- [ ] **Step 1: Replace `AviationCatalogTest` with**

```kotlin
package com.cabin.trivia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AviationCatalogTest {

    private val originalIds = listOf(
        "sin", "ams", "jfk", "qantas", "ana", "lufthansa",
        "spirit", "af1", "contrail", "cb"
    )

    @Test
    fun load_meetsDepthAndInvariants() {
        val catalog = AviationCatalog.load()

        assertTrue("size must be 80..100, was ${catalog.size}", catalog.size in 80..100)
        val ids = catalog.map { it.id }
        assertTrue(ids.all { it.isNotBlank() })
        assertEquals("ids must be unique", ids.size, ids.toSet().size)
        assertEquals(Topic.entries.toSet(), catalog.map { it.topic }.toSet())
        Topic.entries.forEach { topic ->
            val n = catalog.count { it.topic == topic }
            assertTrue("$topic must have at least 20 questions, was $n", n >= 20)
        }
        val idSet = ids.toSet()
        originalIds.forEach { id ->
            assertTrue("missing original id $id", id in idSet)
        }
        catalog.forEach { question ->
            assertTrue(question.prompt.isNotBlank())
            assertTrue(question.explanation.isNotBlank())
            assertEquals(4, question.choices.size)
            assertTrue(question.choices.all { it.isNotBlank() })
            assertTrue(question.correctIndex in question.choices.indices)
        }
    }
}
```

- [ ] **Step 2: Run — expect FAIL (size 10, not 80..100)**

```bash
./gradlew :app:testDebugUnitTest --tests com.cabin.trivia.AviationCatalogTest
```

Expected: FAIL on size.

- [ ] **Step 3: Append the new questions below (keep originals first in each file)**

**Totals after this step: 23 airport, 23 airlines, 22 famous, 22 meteorology = 90.**

Append to `AirportCodesCatalog.kt` after the existing three (do not change `sin`/`ams`/`jfk`):

```kotlin
        Question(
            id = "lhr",
            topic = Topic.AIRPORT_CODES,
            prompt = "What is the IATA code for London Heathrow Airport?",
            choices = listOf("LGW", "LHR", "STN", "LCY"),
            correctIndex = 1,
            explanation = "Heathrow is LHR. LGW is Gatwick, STN is Stansted, and LCY is London City."
        ),
        Question(
            id = "cdg",
            topic = Topic.AIRPORT_CODES,
            prompt = "Paris Charles de Gaulle Airport uses which IATA code?",
            choices = listOf("ORY", "BVA", "CDG", "PAR"),
            correctIndex = 2,
            explanation = "Charles de Gaulle is CDG. ORY is Orly; PAR is a city code, not a single airport."
        ),
        Question(
            id = "dxb",
            topic = Topic.AIRPORT_CODES,
            prompt = "Dubai International Airport's IATA code is:",
            choices = listOf("AUH", "DXB", "DWC", "SHJ"),
            correctIndex = 1,
            explanation = "Dubai International is DXB. DWC is Al Maktoum; AUH is Abu Dhabi; SHJ is Sharjah."
        ),
        Question(
            id = "hnd",
            topic = Topic.AIRPORT_CODES,
            prompt = "Tokyo Haneda Airport uses which IATA code?",
            choices = listOf("NRT", "HND", "KIX", "NGO"),
            correctIndex = 1,
            explanation = "Haneda is HND. NRT is Narita; KIX is Kansai (Osaka)."
        ),
        Question(
            id = "nrt",
            topic = Topic.AIRPORT_CODES,
            prompt = "Narita International Airport, serving Tokyo, is coded:",
            choices = listOf("HND", "NRT", "ITM", "CTS"),
            correctIndex = 1,
            explanation = "Narita is NRT. HND is Haneda, closer to central Tokyo."
        ),
        Question(
            id = "lax",
            topic = Topic.AIRPORT_CODES,
            prompt = "Los Angeles International Airport's IATA code is:",
            choices = listOf("LAX", "BUR", "SNA", "LGB"),
            correctIndex = 0,
            explanation = "Los Angeles International is LAX. BUR is Burbank; SNA is John Wayne (Orange County)."
        ),
        Question(
            id = "ord",
            topic = Topic.AIRPORT_CODES,
            prompt = "Chicago O'Hare International Airport uses which IATA code?",
            choices = listOf("MDW", "ORD", "CHI", "RFD"),
            correctIndex = 1,
            explanation = "O'Hare is ORD (from the old Orchard Field name). MDW is Midway."
        ),
        Question(
            id = "atl",
            topic = Topic.AIRPORT_CODES,
            prompt = "Hartsfield-Jackson Atlanta International Airport is coded:",
            choices = listOf("ATL", "SAV", "PDK", "AHN"),
            correctIndex = 0,
            explanation = "Hartsfield-Jackson Atlanta is ATL. PDK is DeKalb-Peachtree."
        ),
        Question(
            id = "syd",
            topic = Topic.AIRPORT_CODES,
            prompt = "Sydney Kingsford Smith Airport's IATA code is:",
            choices = listOf("MEL", "BNE", "SYD", "CBR"),
            correctIndex = 2,
            explanation = "Sydney is SYD. MEL is Melbourne; BNE is Brisbane; CBR is Canberra."
        ),
        Question(
            id = "hkg",
            topic = Topic.AIRPORT_CODES,
            prompt = "Hong Kong International Airport uses which IATA code?",
            choices = listOf("HKG", "HHP", "MFM", "SZX"),
            correctIndex = 0,
            explanation = "Hong Kong International is HKG. MFM is Macau; SZX is Shenzhen."
        ),
        Question(
            id = "icn",
            topic = Topic.AIRPORT_CODES,
            prompt = "Seoul Incheon International Airport is coded:",
            choices = listOf("GMP", "ICN", "PUS", "CJU"),
            correctIndex = 1,
            explanation = "Incheon is ICN. GMP is Gimpo, closer to central Seoul."
        ),
        Question(
            id = "bkk",
            topic = Topic.AIRPORT_CODES,
            prompt = "Bangkok Suvarnabhumi Airport's IATA code is:",
            choices = listOf("DMK", "BKK", "CNX", "HKT"),
            correctIndex = 1,
            explanation = "Suvarnabhumi is BKK. DMK is Don Mueang, Bangkok's older airport."
        ),
        Question(
            id = "mad",
            topic = Topic.AIRPORT_CODES,
            prompt = "Madrid-Barajas Adolfo Suárez Airport uses which IATA code?",
            choices = listOf("BCN", "MAD", "AGP", "VLC"),
            correctIndex = 1,
            explanation = "Madrid-Barajas is MAD. BCN is Barcelona-El Prat."
        ),
        Question(
            id = "fco",
            topic = Topic.AIRPORT_CODES,
            prompt = "Rome Fiumicino Airport's IATA code is:",
            choices = listOf("CIA", "FCO", "NAP", "VCE"),
            correctIndex = 1,
            explanation = "Fiumicino is FCO. CIA is Ciampino, Rome's secondary airport."
        ),
        Question(
            id = "doh",
            topic = Topic.AIRPORT_CODES,
            prompt = "Hamad International Airport in Doha is coded:",
            choices = listOf("DIA", "DOH", "OTA", "BAH"),
            correctIndex = 1,
            explanation = "Doha's Hamad International is DOH. BAH is Bahrain."
        ),
        Question(
            id = "gru",
            topic = Topic.AIRPORT_CODES,
            prompt = "São Paulo-Guarulhos International Airport uses which IATA code?",
            choices = listOf("CGH", "VCP", "GRU", "GIG"),
            correctIndex = 2,
            explanation = "Guarulhos is GRU. CGH is Congonhas; GIG is Rio Galeão."
        ),
        Question(
            id = "eze",
            topic = Topic.AIRPORT_CODES,
            prompt = "Buenos Aires Ministro Pistarini (Ezeiza) Airport is coded:",
            choices = listOf("AEP", "EZE", "COR", "MVD"),
            correctIndex = 1,
            explanation = "Ezeiza is EZE. AEP is Aeroparque Jorge Newbery, closer to downtown."
        ),
        Question(
            id = "mex",
            topic = Topic.AIRPORT_CODES,
            prompt = "Mexico City International Airport's IATA code is:",
            choices = listOf("MEX", "NLU", "CUN", "GDL"),
            correctIndex = 0,
            explanation = "Mexico City International (Benito Juárez) is MEX. NLU is Felipe Ángeles; CUN is Cancún."
        ),
        Question(
            id = "cph",
            topic = Topic.AIRPORT_CODES,
            prompt = "Copenhagen Airport (Kastrup) uses which IATA code?",
            choices = listOf("ARN", "OSL", "CPH", "BLL"),
            correctIndex = 2,
            explanation = "Copenhagen Kastrup is CPH. ARN is Stockholm Arlanda; OSL is Oslo."
        ),
        Question(
            id = "zrh",
            topic = Topic.AIRPORT_CODES,
            prompt = "Zurich Airport's IATA code is:",
            choices = listOf("GVA", "BSL", "BRN", "ZRH"),
            correctIndex = 3,
            explanation = "Zurich is ZRH. GVA is Geneva; BSL is EuroAirport Basel."
        ),
```

That is 3 original + 20 new = 23 airport questions.

Append to `AirlinesCatalog.kt` after qantas/ana/lufthansa:

```kotlin
        Question(
            id = "emirates-ek",
            topic = Topic.AIRLINES,
            prompt = "What is Emirates' IATA airline code?",
            choices = listOf("QR", "EY", "EK", "FZ"),
            correctIndex = 2,
            explanation = "Emirates is EK. QR is Qatar Airways; EY is Etihad; FZ is flydubai."
        ),
        Question(
            id = "sq",
            topic = Topic.AIRLINES,
            prompt = "Singapore Airlines uses which IATA code?",
            choices = listOf("SQ", "MI", "TR", "3K"),
            correctIndex = 0,
            explanation = "Singapore Airlines is SQ. MI is SilkAir (now merged); TR is Scoot."
        ),
        Question(
            id = "ba",
            topic = Topic.AIRLINES,
            prompt = "British Airways' IATA code is:",
            choices = listOf("VS", "BA", "BD", "U2"),
            correctIndex = 1,
            explanation = "British Airways is BA. VS is Virgin Atlantic; U2 is easyJet."
        ),
        Question(
            id = "af",
            topic = Topic.AIRLINES,
            prompt = "Air France uses which IATA code?",
            choices = listOf("AF", "TO", "SS", "XK"),
            correctIndex = 0,
            explanation = "Air France is AF. It is in a combined group with KLM."
        ),
        Question(
            id = "kl",
            topic = Topic.AIRLINES,
            prompt = "KLM Royal Dutch Airlines' IATA code is:",
            choices = listOf("KL", "HV", "OR", "WA"),
            correctIndex = 0,
            explanation = "KLM is KL (Koninklijke Luchtvaart Maatschappij). HV is Transavia."
        ),
        Question(
            id = "ua",
            topic = Topic.AIRLINES,
            prompt = "United Airlines uses which IATA code?",
            choices = listOf("DL", "AA", "UA", "WN"),
            correctIndex = 2,
            explanation = "United is UA. DL is Delta; AA is American; WN is Southwest."
        ),
        Question(
            id = "dl",
            topic = Topic.AIRLINES,
            prompt = "Delta Air Lines' IATA code is:",
            choices = listOf("DL", "DA", "DE", "NW"),
            correctIndex = 0,
            explanation = "Delta is DL. NW was Northwest, which merged into Delta."
        ),
        Question(
            id = "aa",
            topic = Topic.AIRLINES,
            prompt = "American Airlines uses which IATA code?",
            choices = listOf("AM", "AA", "AS", "B6"),
            correctIndex = 1,
            explanation = "American Airlines is AA. AM is Aeroméxico; AS is Alaska; B6 is JetBlue."
        ),
        Question(
            id = "wn",
            topic = Topic.AIRLINES,
            prompt = "Southwest Airlines' IATA code is:",
            choices = listOf("SW", "WN", "NK", "F9"),
            correctIndex = 1,
            explanation = "Southwest is WN (from the former AirTran/company history of 'WN'). NK is Spirit; F9 is Frontier."
        ),
        Question(
            id = "fr",
            topic = Topic.AIRLINES,
            prompt = "Ryanair uses which IATA code?",
            choices = listOf("FR", "RK", "U2", "W6"),
            correctIndex = 0,
            explanation = "Ryanair is FR. U2 is easyJet; W6 is Wizz Air."
        ),
        Question(
            id = "cx",
            topic = Topic.AIRLINES,
            prompt = "Cathay Pacific's IATA code is:",
            choices = listOf("KA", "CX", "HX", "UO"),
            correctIndex = 1,
            explanation = "Cathay Pacific is CX. KA was Cathay Dragon; HX is Hong Kong Airlines."
        ),
        Question(
            id = "jl",
            topic = Topic.AIRLINES,
            prompt = "Japan Airlines uses which IATA code?",
            choices = listOf("NH", "JL", "NQ", "MM"),
            correctIndex = 1,
            explanation = "Japan Airlines is JL. NH is ANA."
        ),
        Question(
            id = "ke",
            topic = Topic.AIRLINES,
            prompt = "Korean Air's IATA code is:",
            choices = listOf("OZ", "KE", "LJ", "ZE"),
            correctIndex = 1,
            explanation = "Korean Air is KE. OZ is Asiana."
        ),
        Question(
            id = "tk",
            topic = Topic.AIRLINES,
            prompt = "Turkish Airlines uses which IATA code?",
            choices = listOf("TK", "PC", "XQ", "KK"),
            correctIndex = 0,
            explanation = "Turkish Airlines is TK. PC is Pegasus."
        ),
        Question(
            id = "qr",
            topic = Topic.AIRLINES,
            prompt = "Qatar Airways' IATA code is:",
            choices = listOf("EK", "QR", "EY", "GF"),
            correctIndex = 1,
            explanation = "Qatar Airways is QR. EK is Emirates; EY is Etihad; GF is Gulf Air."
        ),
        Question(
            id = "ey",
            topic = Topic.AIRLINES,
            prompt = "Etihad Airways uses which IATA code?",
            choices = listOf("EY", "EK", "WY", "SV"),
            correctIndex = 0,
            explanation = "Etihad (Abu Dhabi) is EY. EK is Emirates; SV is Saudia."
        ),
        Question(
            id = "ac",
            topic = Topic.AIRLINES,
            prompt = "Which airline's livery features a maple leaf?",
            choices = listOf("Air France", "Air Canada", "Lufthansa", "Qantas"),
            correctIndex = 1,
            explanation = "Air Canada's livery uses a maple leaf. Its IATA code is AC."
        ),
        Question(
            id = "sk",
            topic = Topic.AIRLINES,
            prompt = "SAS (Scandinavian Airlines) uses which IATA code?",
            choices = listOf("SK", "DY", "D8", "WF"),
            correctIndex = 0,
            explanation = "SAS is SK. DY is Norwegian; WF is Widerøe."
        ),
        Question(
            id = "et",
            topic = Topic.AIRLINES,
            prompt = "Ethiopian Airlines' IATA code is:",
            choices = listOf("ET", "MS", "SA", "KQ"),
            correctIndex = 0,
            explanation = "Ethiopian is ET. MS is EgyptAir; SA is South African; KQ is Kenya Airways."
        ),
        Question(
            id = "ib",
            topic = Topic.AIRLINES,
            prompt = "Iberia uses which IATA code?",
            choices = listOf("UX", "IB", "VY", "I2"),
            correctIndex = 1,
            explanation = "Iberia is IB. VY is Vueling; UX is Air Europa."
        ),
```

3 original + 20 new = 23 airlines.

Append FamousFlightsCatalog after spirit/af1 (20 new = 22 total):

```kotlin
        Question(
            id = "wright-1903",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The Wright brothers' first powered flight at Kitty Hawk was in which year?",
            choices = listOf("1899", "1903", "1908", "1914"),
            correctIndex = 1,
            explanation = "Orville Wright flew the Wright Flyer on 17 December 1903 at Kitty Hawk, North Carolina."
        ),
        Question(
            id = "earhart-1937",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Amelia Earhart disappeared in 1937 during an attempt to:",
            choices = listOf("Cross the Atlantic solo", "Fly around the world", "Reach the South Pole", "Break the sound barrier"),
            correctIndex = 1,
            explanation = "Earhart and navigator Fred Noonan vanished over the central Pacific on a world-flight attempt."
        ),
        Question(
            id = "yeager-x1",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Who first broke the sound barrier in level flight, in the Bell X-1?",
            choices = listOf("Neil Armstrong", "Chuck Yeager", "John Glenn", "Scott Crossfield"),
            correctIndex = 1,
            explanation = "Chuck Yeager exceeded Mach 1 on 14 October 1947 in the Bell X-1 Glamorous Glennis."
        ),
        Question(
            id = "alcock-brown",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The first nonstop transatlantic flight (1919) was flown by:",
            choices = listOf("Lindbergh", "Alcock and Brown", "Earhart", "the Wright brothers"),
            correctIndex = 1,
            explanation = "John Alcock and Arthur Brown flew a Vickers Vimy from Newfoundland to Ireland in June 1919."
        ),
        Question(
            id = "bleriot-1909",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Louis Blériot is famous for the first airplane crossing of:",
            choices = listOf("The Atlantic", "The English Channel", "The Mediterranean", "The Alps"),
            correctIndex = 1,
            explanation = "Blériot crossed the English Channel from Calais to Dover on 25 July 1909."
        ),
        Question(
            id = "hudson-1549",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "US Airways Flight 1549 in 2009 ditched on which river after a bird strike?",
            choices = listOf("The East River", "The Hudson River", "The Potomac", "The Delaware"),
            correctIndex = 1,
            explanation = "Chesley Sullenberger ditched in the Hudson River off Manhattan; all 155 aboard survived."
        ),
        Question(
            id = "gimli-143",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The 'Gimli Glider' was which Air Canada flight that ran out of fuel in 1983?",
            choices = listOf("Flight 621", "Flight 143", "Flight 797", "Flight 189"),
            correctIndex = 1,
            explanation = "Air Canada 143, a 767, glided to a landing at Gimli, Manitoba, after a metric fuel-loading error."
        ),
        Question(
            id = "tenerife-1977",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The 1977 Tenerife disaster involved a runway collision between KLM and which other airline?",
            choices = listOf("TWA", "Pan Am", "British Airways", "Iberia"),
            correctIndex = 1,
            explanation = "KLM 4805 and Pan Am 1736 collided on the runway at Los Rodeos; it remains the deadliest accident in aviation."
        ),
        Question(
            id = "concorde-2003",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Concorde's last commercial passenger flights were in which year?",
            choices = listOf("1999", "2001", "2003", "2006"),
            correctIndex = 2,
            explanation = "Air France and British Airways retired Concorde in 2003 after the 2000 Paris crash and rising costs."
        ),
        Question(
            id = "voyager-1986",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The first unrefueled nonstop flight around the world (1986) was in the aircraft named:",
            choices = listOf("Spirit of St. Louis", "Voyager", "GlobalFlyer", "Graf Zeppelin"),
            correctIndex = 1,
            explanation = "Dick Rutan and Jeana Yeager flew the Rutan Voyager around the world in nine days in December 1986."
        ),
        Question(
            id = "hindenburg-1937",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "The Hindenburg airship disaster in 1937 occurred at:",
            choices = listOf("Lakehurst, New Jersey", "Friedrichshafen", "Cardington", "Akron"),
            correctIndex = 0,
            explanation = "LZ 129 Hindenburg caught fire while landing at Lakehurst on 6 May 1937."
        ),
        Question(
            id = "panam-103",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Pan Am Flight 103 was destroyed by a bomb over which town in 1988?",
            choices = listOf("Lockerbie", "Lockerbie's neighbor Moffat", "Prestwick", "Shannon"),
            correctIndex = 0,
            explanation = "The 747 was destroyed over Lockerbie, Scotland, on 21 December 1988."
        ),
        Question(
            id = "ual-232",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "United Airlines Flight 232 (1989) lost all hydraulics and landed at:",
            choices = listOf("Sioux City, Iowa", "Denver", "Chicago O'Hare", "Minneapolis"),
            correctIndex = 0,
            explanation = "After an uncontained engine failure, the DC-10 was crash-landed at Sioux Gateway Airport."
        ),
        Question(
            id = "qf32",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Qantas Flight 32 in 2010 was an A380 that suffered an uncontained engine failure after departing:",
            choices = listOf("Sydney", "Singapore", "London", "Los Angeles"),
            correctIndex = 1,
            explanation = "QF32 left Singapore for Sydney; the crew returned to Singapore after a Trent 900 failure."
        ),
        Question(
            id = "ba009-ash",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "British Airways Flight 009 in 1982 lost all four engines after flying through:",
            choices = listOf("A hailstorm", "Volcanic ash", "Severe icing", "A microburst"),
            correctIndex = 1,
            explanation = "The 747 flew through ash from Mount Galunggung; engines were restarted and it landed at Jakarta."
        ),
        Question(
            id = "kal-007",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Korean Air Lines Flight 007 was shot down in 1983 after straying into which country's airspace?",
            choices = listOf("China", "the Soviet Union", "North Korea", "Japan"),
            correctIndex = 1,
            explanation = "KAL 007 was shot down by a Soviet fighter over Sakhalin after a navigation error."
        ),
        Question(
            id = "db-cooper",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "D. B. Cooper is known for hijacking a Northwest Orient 727 in 1971 and then:",
            choices = listOf("Landing in Cuba", "Parachuting from the aircraft", "Crashing in the Rockies", "Surrendering in Seattle"),
            correctIndex = 1,
            explanation = "After exchanging passengers for ransom in Seattle, Cooper jumped from the 727 over the Pacific Northwest and was never found."
        ),
        Question(
            id = "flight-19",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Flight 19 was a 1945 U.S. Navy trainer formation that disappeared over:",
            choices = listOf("The Bermuda Triangle area of the Atlantic", "the Pacific off California", "Lake Michigan", "the North Sea"),
            correctIndex = 0,
            explanation = "Five TBM Avengers vanished east of Florida; a search plane was also lost."
        ),
        Question(
            id = "sts-107",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Space Shuttle Columbia was lost during reentry on mission:",
            choices = listOf("STS-51-L", "STS-107", "STS-1", "STS-135"),
            correctIndex = 1,
            explanation = "STS-107 broke up over Texas on 1 February 2003. STS-51-L was Challenger."
        ),
        Question(
            id = "earhart-1932",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Who was the first woman to fly solo nonstop across the Atlantic (1932)?",
            choices = listOf("Bessie Coleman", "Amelia Earhart", "Amy Johnson", "Harriet Quimby"),
            correctIndex = 1,
            explanation = "Earhart flew a Lockheed Vega from Newfoundland to Northern Ireland in May 1932."
        ),
```

Append MeteorologyCatalog after contrail/cb (20 new = 22):

```kotlin
        Question(
            id = "jet-stream",
            topic = Topic.METEOROLOGY,
            prompt = "The jet stream is best described as:",
            choices = listOf("A surface trade wind", "A narrow band of strong winds near the tropopause", "A thunderstorm outflow", "A mountain breeze"),
            correctIndex = 1,
            explanation = "Jet streams are fast, narrow west-to-east winds near the tropopause that airliners use or avoid."
        ),
        Question(
            id = "cat",
            topic = Topic.METEOROLOGY,
            prompt = "Clear-air turbulence (CAT) typically occurs:",
            choices = listOf("Only inside thunderstorms", "In cloud-free air, often near jet streams", "Only below 10,000 feet", "Only over oceans"),
            correctIndex = 1,
            explanation = "CAT is turbulence without nearby clouds, often in shear near the jet stream."
        ),
        Question(
            id = "wind-shear",
            topic = Topic.METEOROLOGY,
            prompt = "Wind shear is:",
            choices = listOf("Steady headwind on final", "A sudden change in wind speed or direction", "Only a high-altitude phenomenon", "The same as wake turbulence"),
            correctIndex = 1,
            explanation = "Shear is an abrupt wind-vector change; low-level shear on approach is especially dangerous."
        ),
        Question(
            id = "microburst",
            topic = Topic.METEOROLOGY,
            prompt = "A microburst is:",
            choices = listOf("A small tornado", "An intense, localized downdraft", "A type of icing", "A jet-stream core"),
            correctIndex = 1,
            explanation = "A microburst is a strong, short-lived downdraft that can cause a sudden performance loss on takeoff or landing."
        ),
        Question(
            id = "icing",
            topic = Topic.METEOROLOGY,
            prompt = "Airframe icing is most likely when flying through:",
            choices = listOf("Dry snow", "Supercooled liquid water droplets", "Pure ice crystals at −60°C", "Warm rain above 10°C"),
            correctIndex = 1,
            explanation = "Supercooled droplets freeze on impact. Very cold ice-crystal clouds often produce less accretion."
        ),
        Question(
            id = "wake-turb",
            topic = Topic.METEOROLOGY,
            prompt = "Wake turbulence behind a heavy jet is mainly caused by:",
            choices = listOf("Engine exhaust only", "Wingtip vortices from lift", "Landing-gear drag", "Spoilers"),
            correctIndex = 1,
            explanation = "Lift produces counter-rotating wingtip vortices; they sink and persist, especially behind heavy, clean aircraft."
        ),
        Question(
            id = "tropopause",
            topic = Topic.METEOROLOGY,
            prompt = "The tropopause is the boundary between:",
            choices = listOf("Troposphere and stratosphere", "Stratosphere and mesosphere", "Boundary layer and free atmosphere", "Ionosphere and space"),
            correctIndex = 0,
            explanation = "The tropopause caps the troposphere; jet streams and much cruise traffic sit near it."
        ),
        Question(
            id = "isa-15",
            topic = Topic.METEOROLOGY,
            prompt = "ISA sea-level temperature is:",
            choices = listOf("0°C", "15°C", "20°C", "59°C"),
            correctIndex = 1,
            explanation = "The International Standard Atmosphere uses 15°C (59°F) and 1013.25 hPa at mean sea level."
        ),
        Question(
            id = "qnh",
            topic = Topic.METEOROLOGY,
            prompt = "When an altimeter is set to QNH, it reads approximately:",
            choices = listOf("Height above the airfield when on the ground", "Altitude above mean sea level", "Flight level", "Cabin altitude"),
            correctIndex = 1,
            explanation = "QNH is the pressure setting so the altimeter shows elevation AMSL on the ground at that airport."
        ),
        Question(
            id = "metar",
            topic = Topic.METEOROLOGY,
            prompt = "A METAR is:",
            choices = listOf("A 24-hour aerodrome forecast", "A routine aviation weather observation", "A volcanic-ash advisory", "A NOTAM"),
            correctIndex = 1,
            explanation = "METARs are coded routine observations; TAFs are forecasts."
        ),
        Question(
            id = "taf",
            topic = Topic.METEOROLOGY,
            prompt = "A TAF is:",
            choices = listOf("A terminal aerodrome forecast", "A takeoff alternate formula", "A turbulence advisory format", "A taxiway assignment"),
            correctIndex = 0,
            explanation = "TAF means Terminal Aerodrome Forecast, issued for a specific airport over a period of hours."
        ),
        Question(
            id = "rvr",
            topic = Topic.METEOROLOGY,
            prompt = "RVR stands for:",
            choices = listOf("Radar velocity range", "Runway visual range", "Regional vortex report", "Required visibility rating"),
            correctIndex = 1,
            explanation = "Runway visual range is the distance a pilot can see down the runway from the threshold."
        ),
        Question(
            id = "mountain-wave",
            topic = Topic.METEOROLOGY,
            prompt = "Mountain wave turbulence is found:",
            choices = listOf("Only on the windward slope at ground level", "Downwind of ridges in stable flow, sometimes far downstream", "Only in the tropics", "Only inside cumulonimbus"),
            correctIndex = 1,
            explanation = "Waves and rotors form in the lee of mountains when a stable airstream is forced over terrain."
        ),
        Question(
            id = "itcz",
            topic = Topic.METEOROLOGY,
            prompt = "The ITCZ is:",
            choices = listOf("A polar front", "The Intertropical Convergence Zone of thunderstorms near the equator", "A North Atlantic jet", "A type of icing"),
            correctIndex = 1,
            explanation = "Trade winds converge near the equator in the ITCZ, producing widespread convective weather."
        ),
        Question(
            id = "dewpoint",
            topic = Topic.METEOROLOGY,
            prompt = "Dewpoint is the temperature at which:",
            choices = listOf("Water boils at altitude", "Air becomes saturated if cooled at constant pressure", "Jet fuel freezes", "ISA temperature is defined"),
            correctIndex = 1,
            explanation = "When temperature equals dewpoint, relative humidity is 100% and fog or cloud can form."
        ),
        Question(
            id = "std-pressure",
            topic = Topic.METEOROLOGY,
            prompt = "Standard sea-level pressure used for flight levels is:",
            choices = listOf("29.92 inHg / 1013.25 hPa", "28.92 inHg", "30.50 inHg", "1000.00 hPa exactly"),
            correctIndex = 0,
            explanation = "Above the transition altitude, altimeters are set to 29.92 inHg (1013.25 hPa) to fly flight levels."
        ),
        Question(
            id = "virga",
            topic = Topic.METEOROLOGY,
            prompt = "Virga is:",
            choices = listOf("Hail on the runway", "Precipitation that evaporates before reaching the ground", "A type of lightning", "Freezing rain"),
            correctIndex = 1,
            explanation = "Virga can still produce dangerous downdrafts even if rain does not reach the surface."
        ),
        Question(
            id = "inversion",
            topic = Topic.METEOROLOGY,
            prompt = "A temperature inversion is a layer where:",
            choices = listOf("Temperature decreases rapidly with height", "Temperature increases with height", "Wind is always calm", "Clouds cannot form"),
            correctIndex = 1,
            explanation = "In an inversion, warmer air sits above cooler air; it can trap pollutants and affect climb performance."
        ),
        Question(
            id = "advection-fog",
            topic = Topic.METEOROLOGY,
            prompt = "Advection fog commonly forms when:",
            choices = listOf("Warm moist air moves over a colder surface", "The ground radiates heat on a clear night only", "A thunderstorm outflows", "Jet stream cirrus thickens"),
            correctIndex = 0,
            explanation = "Advection fog is typical when warm, moist air streams over a cold sea or snow-covered ground."
        ),
        Question(
            id = "hail",
            topic = Topic.METEOROLOGY,
            prompt = "Hail that can damage aircraft is produced mainly in:",
            choices = listOf("Stratus", "Cumulonimbus with strong updrafts", "Cirrus", "Fog"),
            correctIndex = 1,
            explanation = "Strong CB updrafts recycle supercooled water until ice pellets grow large enough to fall as hail."
        ),
```

- [ ] **Step 4: Run tests**

```bash
./gradlew :app:testDebugUnitTest
```

Expected: BUILD SUCCESSFUL. Catalog size 90, all four topics ≥20, original 10 ids present, unique ids.

If size is off, count `id =` in the four files. If a test fails on uniqueness, find the duplicate slug.

- [ ] **Step 5: Commit**

```bash
git add app/src/test/java/com/cabin/trivia/AviationCatalogTest.kt \
  app/src/main/java/com/cabin/trivia/AirportCodesCatalog.kt \
  app/src/main/java/com/cabin/trivia/AirlinesCatalog.kt \
  app/src/main/java/com/cabin/trivia/FamousFlightsCatalog.kt \
  app/src/main/java/com/cabin/trivia/MeteorologyCatalog.kt
git commit -m "Expand bundled catalog to 90 aviation questions across four topics"
```

---

### Task 4: README matches play

**Files:**
- Modify: `README.md`

- [ ] **Step 1: Replace the "What it does" section (keep the toolchain block)**

```markdown
# Cabin Trivia

Free, fully offline aviation trivia for a 1–2 hour flight. Questions ship in the APK. No account, no network.

## What it does

- Loads a bundled catalog of 80–100 questions (airport codes, airlines, famous flights, meteorology)
- Shuffles the full catalog each Play / Play again
- Tap a choice to reveal: the miss and the correct answer are marked, plus a short explanation; then Continue
- Score is shown as You got X of Y
- Rotation restores the same deal (no network)

## Toolchain
```

Keep the existing export / `./gradlew` / `adb` instructions unchanged.

- [ ] **Step 2: Confirm no INTERNET in the manifest**

```bash
grep INTERNET app/src/main/AndroidManifest.xml || echo "no INTERNET permission"
./gradlew :app:assembleDebug
```

Expected: no INTERNET; assembleDebug BUILD SUCCESSFUL.

- [ ] **Step 3: Commit**

```bash
git add README.md
git commit -m "Document shuffle, reveal, and catalog size in the README"
```

---

## Spec coverage

| Spec item | Task |
|---|---|
| Non-blank id | 1 |
| Split four files + concatenate load() | 2 |
| 80–100, ≥20/topic, original 10 ids, unique ids | 3 |
| README play path | 4 |
| assembleDebug, no INTERNET | 4 |
| No session-engine / packs / JSON | throughout |

## Note

`atl` must not use duplicate `"ATL"` choices. Famous-flight `panam-103` wrong answers should stay distinct; if a compiler/test complains about any duplicate **id**, rename the slug, not the original ten.
