package com.cabin.trivia

internal val famousFlightsQuestions: List<Question> = listOf(
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
    )
)
