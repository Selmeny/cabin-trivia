package com.cabin.trivia

internal val airlinesQuestions: List<Question> = listOf(
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
    )
)
