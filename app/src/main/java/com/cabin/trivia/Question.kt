package com.cabin.trivia

data class Question(
    val id: String,
    val topic: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int
)
