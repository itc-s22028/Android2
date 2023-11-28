package jp.ac.it_college.std.s22028.pokemon_api.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Generation (
    val id: Int,
    val name: String,
    val names: List<Name>,
    @SerialName("pokemon_species") val pokemonSpecies: List<NamedAPIResourse>
    )