package com.example.pokedextp

import com.squareup.sqldelight.db.SqlDriver
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun initLogger(){
    Napier.base(DebugAntilog())
}

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver{
        return NativeSqliteDriver(MyDatabase.Schema, "test.db")
    }
}