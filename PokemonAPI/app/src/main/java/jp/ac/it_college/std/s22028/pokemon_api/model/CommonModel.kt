package jp.ac.it_college.std.s22028.pokemon_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// どれだけ使うか、いま時点で不明だけど全部定義

// ResourceLists/Pagination
@Serializable
data class NamedApiResourceList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedApiResource>,
)

// Utility Languages
@Serializable
data class Language(
    val id: Int,
    val name: String,
    val official: Boolean,
    val iso639: String,
    val iso3166: String,
    val names: List<Name>,
)

// Utility Common Models
@Serializable
data class ApiResource(
    val url: String,
)

@Serializable
data class Description(
    val description: String,
    val language: NamedApiResource,
)

@Serializable
data class Effect(
    val effect: String,
    val language: NamedApiResource,
)

@Serializable
data class Encounter(
    @SerialName("min_level") val minLevel: Int,
    @SerialName("max_level") val maxLevel: Int,
    @SerialName("condition_values") val conditionValues: List<NamedApiResource>,
    val chance: Int,
    val method: NamedApiResource,
)

@Serializable
data class FlavorText(
    @SerialName("flavor_text") val flavorText: String,
    val language: NamedApiResource,
    val version: NamedApiResource,
)

@Serializable
data class GenerationGameIndex(
    @SerialName("game_index") val gameIndex: Int,
    val generation: NamedApiResource,
)

@Serializable
data class MachineVersionDetail(
    val machine: NamedApiResource,
    @SerialName("version_group") val versionGroup: NamedApiResource,
)

@Serializable
data class Name(
    val name: String,
    val language: NamedApiResource,
)

@Serializable
data class NamedApiResource(
    val name: String,
    val url: String,
)

@Serializable
data class VerboseEffect(
    val effect: String,
    @SerialName("short_effect") val shortEffect: String,
    val language: NamedApiResource,
)

@Serializable
data class VersionEncounterDetail(
    val version: NamedApiResource,
    @SerialName("max_chance") val maxChance: Int,
    @SerialName("encounter_details") val encounterDetails: List<NamedApiResource>,
)

@Serializable
data class VersionGameIndex(
    @SerialName("game_index") val gameIndex: Int,
    val version: NamedApiResource,
)

@Serializable
data class VersionGroupFlavorText(
    val text: String,
    val language: NamedApiResource,
)