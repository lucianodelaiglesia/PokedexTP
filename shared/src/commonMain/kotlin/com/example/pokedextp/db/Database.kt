package com.example.pokedextp.db

import com.example.pokedextp.DatabaseDriverFactory
import com.example.pokedextp.MyDatabase
import com.example.pokedextp.db.model.PokedexResults

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = MyDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.myDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.deleteAllPokemon()
        }
    }

    internal fun getAllPokemon(): List<PokedexResults> {
        return dbQuery.selectAllPokemon(::mapPokemonSelecting).executeAsList()
    }

    private fun mapPokemonSelecting(
        mapName: String,
        mapUrl: String
    ): PokedexResults {
        return PokedexResults(name = mapName, url = mapUrl)
    }

    internal fun createPokemon(pokemon: List<PokedexResults>){
        dbQuery.transaction {
            pokemon.forEach { it ->
                addPokemon(it)
            }
        }
    }

    private fun addPokemon(pokemon: PokedexResults) {
        dbQuery.insertPokemon(name = pokemon.name, url = pokemon.url)
    }
}