package com.cabin.trivia

enum class Topic {
    AIRPORT_CODES,
    AIRLINES,
    FAMOUS_FLIGHTS,
    METEOROLOGY
}

data class Question(
    val id: String,
    val topic: Topic,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String
) {
    init {
        require(id.isNotBlank()) { "id must be non-blank" }
        require(prompt.isNotBlank()) { "prompt must be non-blank" }
        require(explanation.isNotBlank()) { "explanation must be non-blank" }
        require(choices.size == 4) { "Question must have exactly four choices" }
        require(choices.all { it.isNotBlank() }) { "choices must be non-blank" }
        require(correctIndex in choices.indices) { "correctIndex out of range" }
    }
}
