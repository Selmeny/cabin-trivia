package com.cabin.trivia

object AviationCatalog {
    const val TOPIC_AIRPORT_CODES = "airport_codes"
    const val TOPIC_AIRLINES = "airlines"
    const val TOPIC_FAMOUS_FLIGHTS = "famous_flights"
    const val TOPIC_METEOROLOGY = "meteorology"

    fun load(): List<Question> = listOf(
        Question(
            id = "sin",
            topic = TOPIC_AIRPORT_CODES,
            prompt = "What is the IATA code for Singapore Changi Airport?",
            choices = listOf("SGN", "SIN", "CGK", "KUL"),
            correctIndex = 1
        ),
        Question(
            id = "ams",
            topic = TOPIC_AIRPORT_CODES,
            prompt = "Amsterdam Schiphol Airport uses which IATA code?",
            choices = listOf("AMS", "ARN", "BRU", "RTM"),
            correctIndex = 0
        ),
        Question(
            id = "jfk",
            topic = TOPIC_AIRPORT_CODES,
            prompt = "JFK is the IATA code for which city's main international airport?",
            choices = listOf("Los Angeles", "Chicago", "New York", "Miami"),
            correctIndex = 2
        ),
        Question(
            id = "qantas",
            topic = TOPIC_AIRLINES,
            prompt = "Which airline's livery features a kangaroo on the tail?",
            choices = listOf("Air New Zealand", "Qantas", "Emirates", "Cathay Pacific"),
            correctIndex = 1
        ),
        Question(
            id = "ana",
            topic = TOPIC_AIRLINES,
            prompt = "ANA is the IATA code for which airline?",
            choices = listOf("Air New Zealand", "Austrian Airlines", "All Nippon Airways", "Air North"),
            correctIndex = 2
        ),
        Question(
            id = "lufthansa",
            topic = TOPIC_AIRLINES,
            prompt = "Lufthansa's main hub is which airport?",
            choices = listOf("Munich (MUC)", "Berlin Brandenburg (BER)", "Frankfurt (FRA)", "Düsseldorf (DUS)"),
            correctIndex = 2
        ),
        Question(
            id = "spirit",
            topic = TOPIC_FAMOUS_FLIGHTS,
            prompt = "Who piloted the Spirit of St. Louis on the first solo nonstop transatlantic flight?",
            choices = listOf("Amelia Earhart", "Charles Lindbergh", "Wiley Post", "Howard Hughes"),
            correctIndex = 1
        ),
        Question(
            id = "af1",
            topic = TOPIC_FAMOUS_FLIGHTS,
            prompt = "Air Force One is the call sign used when which passenger is aboard a U.S. Air Force aircraft?",
            choices = listOf("The Vice President", "The Secretary of Defense", "The President of the United States", "The Speaker of the House"),
            correctIndex = 2
        ),
        Question(
            id = "contrail",
            topic = TOPIC_METEOROLOGY,
            prompt = "The white trails often seen behind jets at cruise altitude are called:",
            choices = listOf("Wake vortices", "Contrails", "St. Elmo's fire", "Chemtrails (a scientific term)"),
            correctIndex = 1
        ),
        Question(
            id = "cb",
            topic = TOPIC_METEOROLOGY,
            prompt = "Which cloud type is most associated with thunderstorms and severe turbulence?",
            choices = listOf("Cirrus", "Stratus", "Cumulonimbus", "Altocumulus"),
            correctIndex = 2
        )
    )
}
