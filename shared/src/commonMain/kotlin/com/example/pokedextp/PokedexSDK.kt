package com.example.pokedextp

import com.example.pokedextp.db.Database
import com.example.pokedextp.db.model.PokedexResults
import com.example.pokedextp.network.PokedexRepositoryImp

class PokedexSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = PokedexRepositoryImp()

    suspend fun getPokemon(canConnect: Boolean): List<PokedexResults>{
        val cachedPokemon = database.getAllPokemon()
        return if (cachedPokemon.isNotEmpty() && !canConnect) {
            cachedPokemon
        } else if (cachedPokemon.isEmpty() && !canConnect) {
            return emptyList()
        } else {
            api.getPokedex().results.also{
                database.clearDatabase()
                database.createPokemon(it)
            }
        }
    }
}