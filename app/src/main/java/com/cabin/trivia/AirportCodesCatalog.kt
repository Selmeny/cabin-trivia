package com.cabin.trivia

internal val airportCodesQuestions: List<Question> = listOf(
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
    )
)
