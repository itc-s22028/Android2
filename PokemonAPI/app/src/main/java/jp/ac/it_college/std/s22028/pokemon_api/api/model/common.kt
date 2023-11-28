package jp.ac.it_college.std.s22028.pokemon_api.api.model

import kotlinx.serialization.Serializable

@Serializable
data class NamedAPIResourse(
    val name: String,
    val url: String
)

@Serializable
data class Name(
    val name: String,
    val language: NamedAPIResourse
)