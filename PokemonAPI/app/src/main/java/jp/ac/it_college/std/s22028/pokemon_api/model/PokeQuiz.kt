package jp.ac.it_college.std.s22028.pokemon_api.model


data class PokeQuiz(
    val imageUrl: String,
    val choices: List<String>,
    val correct: String
)
