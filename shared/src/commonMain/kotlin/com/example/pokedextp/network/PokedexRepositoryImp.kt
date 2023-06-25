package com.example.pokedextp.network

import com.example.pokedextp.db.model.Pokedex
import com.example.pokedextp.initLogger
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PokedexRepositoryImp {

    val httpClient = HttpClient{
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String){
                    Napier.v(tag = "HttpClient", message = message)
                }
            }
        }
        install(ContentNegotiation) {
            json(
                Json{
                    ignoreUnknownKeys = true
                }
            )
        }
    }.also {
        initLogger()
    }

    suspend fun getPokedex(): Pokedex {
        val result = httpClient.get("https://pokeapi.co/api/v2/pokemon/?limit=800")
        return result.body()
    }
}