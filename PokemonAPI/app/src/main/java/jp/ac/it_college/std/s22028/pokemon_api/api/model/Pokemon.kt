package jp.ac.it_college.std.s22028.pokemon_api.api.model

import jp.ac.it_college.std.s22028.pokemon_api.database.entity.Poke

data class Pokemon(
    val id: Int,
    val sprites: PokemonSprites
)