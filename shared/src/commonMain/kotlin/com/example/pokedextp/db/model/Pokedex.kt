package com.example.pokedextp.db.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokedex(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String,
    //@SerialName("previous")
    //val previous: String,
    @SerialName("results")
    val results: List<PokedexResults>
)
