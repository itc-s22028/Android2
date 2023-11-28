package jp.ac.it_college.std.s22028.pokemon_api.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonSpecies(
    val id: Int,
    val varieties: List<PokemonSpeciesVariety>
)