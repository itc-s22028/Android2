package jp.ac.it_college.std.s22028.ktor_sample.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    @SerialName("coord")
    val coordinates: Coordinates,

    val weather: List<Weather>,

    @SerialName("name")
    val cityName: String,
)

@Serializable
data class Coordinates(
    @SerialName("lon")
    val longitude: Double,

    @SerialName("lat")
    val latiude: Double,
)

@Serializable
data class Weather(
    val id: Int,

    @SerialName("main")
    val groupName: String,

    val description: String,

    val icon: String,
)