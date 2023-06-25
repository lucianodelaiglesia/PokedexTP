package com.example.pokedextp.db.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokedexResults(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)
