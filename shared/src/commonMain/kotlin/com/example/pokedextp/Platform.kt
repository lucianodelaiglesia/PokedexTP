package com.example.pokedextp

import app.cash.sqldelight.db.SqlDriver

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun initLogger()

expect class DatabaseDriverFactory {
   fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DatabaseDriverFactory): MyDatabase {
    val driver = driverFactory.createDriver()
    return MyDatabase(driver)
}