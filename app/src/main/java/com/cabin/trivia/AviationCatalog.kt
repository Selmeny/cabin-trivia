package com.cabin.trivia

object AviationCatalog {
    fun load(): List<Question> =
        airportCodesQuestions +
            airlinesQuestions +
            famousFlightsQuestions +
            meteorologyQuestions
}
