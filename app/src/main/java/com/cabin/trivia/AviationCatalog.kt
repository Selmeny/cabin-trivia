package com.cabin.trivia

object AviationCatalog {
    fun load(): List<Question> = listOf(
        Question(
            id = "sin",
            topic = Topic.AIRPORT_CODES,
            prompt = "What is the IATA code for Singapore Changi Airport?",
            choices = listOf("SGN", "SIN", "CGK", "KUL"),
            correctIndex = 1,
            explanation = "Singapore Changi Airport's IATA code is SIN; SGN is Ho Chi Minh City and CGK is Jakarta."
        ),
        Question(
            id = "ams",
            topic = Topic.AIRPORT_CODES,
            prompt = "Amsterdam Schiphol Airport uses which IATA code?",
            choices = listOf("AMS", "ARN", "BRU", "RTM"),
            correctIndex = 0,
            explanation = "Amsterdam Schiphol uses AMS. ARN is Stockholm Arlanda; RTM is Rotterdam."
        ),
        Question(
            id = "jfk",
            topic = Topic.AIRPORT_CODES,
            prompt = "JFK is the IATA code for which city's main international airport?",
            choices = listOf("Los Angeles", "Chicago", "New York", "Miami"),
            correctIndex = 2,
            explanation = "JFK is John F. Kennedy International in New York. Los Angeles is LAX; Miami is MIA."
        ),
        Question(
            id = "qantas",
            topic = Topic.AIRLINES,
            prompt = "Which airline's livery features a kangaroo on the tail?",
            choices = listOf("Air New Zealand", "Qantas", "Emirates", "Cathay Pacific"),
            correctIndex = 1,
            explanation = "Qantas (Australia) paints a kangaroo on the tail. Air New Zealand uses a koru."
        ),
        Question(
            id = "ana",
            topic = Topic.AIRLINES,
            prompt = "ANA is the IATA code for which airline?",
            choices = listOf("Air New Zealand", "Austrian Airlines", "All Nippon Airways", "Air North"),
            correctIndex = 2,
            explanation = "ANA stands for All Nippon Airways, Japan's largest airline by fleet."
        ),
        Question(
            id = "lufthansa",
            topic = Topic.AIRLINES,
            prompt = "Lufthansa's main hub is which airport?",
            choices = listOf("Munich (MUC)", "Berlin Brandenburg (BER)", "Frankfurt (FRA)", "Düsseldorf (DUS)"),
            correctIndex = 2,
            explanation = "Lufthansa's primary hub is Frankfurt (FRA). Munich (MUC) is the second hub."
        ),
        Question(
            id = "spirit",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Who piloted the Spirit of St. Louis on the first solo nonstop transatlantic flight?",
            choices = listOf("Amelia Earhart", "Charles Lindbergh", "Wiley Post", "Howard Hughes"),
            correctIndex = 1,
            explanation = "Charles Lindbergh flew the Spirit of St. Louis from New York to Paris in 1927."
        ),
        Question(
            id = "af1",
            topic = Topic.FAMOUS_FLIGHTS,
            prompt = "Air Force One is the call sign used when which passenger is aboard a U.S. Air Force aircraft?",
            choices = listOf("The Vice President", "The Secretary of Defense", "The President of the United States", "The Speaker of the House"),
            correctIndex = 2,
            explanation = "Air Force One is the call sign for a U.S. Air Force aircraft carrying the President."
        ),
        Question(
            id = "contrail",
            topic = Topic.METEOROLOGY,
            prompt = "The white trails often seen behind jets at cruise altitude are called:",
            choices = listOf("Wake vortices", "Contrails", "St. Elmo's fire", "Chemtrails (a scientific term)"),
            correctIndex = 1,
            explanation = "Contrails are condensation trails from engine exhaust freezing in cold, humid air at altitude."
        ),
        Question(
            id = "cb",
            topic = Topic.METEOROLOGY,
            prompt = "Which cloud type is most associated with thunderstorms and severe turbulence?",
            choices = listOf("Cirrus", "Stratus", "Cumulonimbus", "Altocumulus"),
            correctIndex = 2,
            explanation = "Cumulonimbus clouds bring thunderstorms, hail, and severe turbulence. Cirrus are high and thin."
        )
    )
}
