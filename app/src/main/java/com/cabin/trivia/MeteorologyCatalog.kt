package com.cabin.trivia

internal val meteorologyQuestions: List<Question> = listOf(
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
